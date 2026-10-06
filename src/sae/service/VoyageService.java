package sae.service;

import fr.ulille.but.sae_s2_2026.Chemin;
import fr.ulille.but.sae_s2_2026.Connexion;
import fr.ulille.but.sae_s2_2026.Lieu;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;
import sae.poo.NoPathFound;
import sae.poo.Plateforme;
import sae.poo.Route;
import sae.poo.TypeCout;
import sae.poo.Ville;
import sae.poo.Voyageur;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class VoyageService {

    private static final String CSV_GRAPHE = "ressources/Poo-graphe.csv";
    private static final String CSV_CORRESPONDANCES = "ressources/Poo-correspondances.csv";

    public List<String> chargerVilles() {
        try {
            List<String> lignes = Files.readAllLines(Path.of(CSV_GRAPHE));
            LinkedHashSet<String> villes = new LinkedHashSet<>();

            for (String ligne : lignes) {
                if (ligne == null || ligne.isBlank()) {
                    continue;
                }

                String[] parties = ligne.split(";");
                if (parties.length >= 2) {
                    String villeDepart = parties[0].trim();
                    String villeArrivee = parties[1].trim();

                    if (!villeDepart.isEmpty()) {
                        villes.add(villeDepart);
                    }
                    if (!villeArrivee.isEmpty()) {
                        villes.add(villeArrivee);
                    }
                }
            }

            return new ArrayList<>(villes);

        } catch (IOException e) {
            throw new RuntimeException("Impossible de lire le fichier : " + CSV_GRAPHE, e);
        }
    }

    /**
     * Recherche de trajets avec une contrainte optionnelle de durée maximale.
     */
    public List<String> rechercher(String nom,
                                   String depart,
                                   String arrivee,
                                   String transportStr,
                                   String critereStr,
                                   int k,
                                   Double dureeMax) {

        ModaliteTransport transport = ModaliteTransport.valueOf(transportStr.toUpperCase());
        TypeCout critere = TypeCout.valueOf(critereStr.toUpperCase());

        Voyageur utilisateur = creerVoyageurSelonCritere(nom, critere);

        if (dureeMax != null) {
            utilisateur.setMaxTemps(dureeMax);
        }

        Plateforme plateforme = new Plateforme(
                CSV_GRAPHE,
                CSV_CORRESPONDANCES,
                utilisateur
        );

        Lieu villeDepart = new Ville(depart);
        Lieu villeArrivee = new Ville(arrivee);

        ArrayList<Chemin> chemins = plateforme.calculerChemins(
                villeDepart,
                villeArrivee,
                k,
                utilisateur
        );

        List<String> resultats = new ArrayList<>();

        for (Chemin chemin : chemins) {
            if (respecteTransport(chemin, transport)) {
                resultats.add(formaterChemin(plateforme, chemin, villeDepart, villeArrivee, critere));
            }
        }

        if (resultats.isEmpty()) {
            throw new NoPathFound("Aucun trajet ne correspond aux critères demandés.");
        }

        return resultats;
    }

    /** Conserve la compatibilité avec un éventuel appel sans contrainte de durée. */
    public List<String> rechercher(String nom,
                                   String depart,
                                   String arrivee,
                                   String transportStr,
                                   String critereStr,
                                   int k) {
        return rechercher(nom, depart, arrivee, transportStr, critereStr, k, null);
    }

    /**
     * Retourne les informations brutes (CO2, prix, temps) de chaque chemin trouvé,
     * utile pour l'affichage en cartes et le nudging écologique.
     */
    public List<ResultatTrajet> rechercherDetaille(String nom,
                                                    String depart,
                                                    String arrivee,
                                                    String transportStr,
                                                    String critereStr,
                                                    int k,
                                                    Double dureeMax) {

        ModaliteTransport transport = ModaliteTransport.valueOf(transportStr.toUpperCase());
        TypeCout critere = TypeCout.valueOf(critereStr.toUpperCase());

        Voyageur utilisateur = creerVoyageurSelonCritere(nom, critere);

        if (dureeMax != null) {
            utilisateur.setMaxTemps(dureeMax);
        }

        Plateforme plateforme = new Plateforme(
                CSV_GRAPHE,
                CSV_CORRESPONDANCES,
                utilisateur
        );

        Lieu villeDepart = new Ville(depart);
        Lieu villeArrivee = new Ville(arrivee);

        ArrayList<Chemin> chemins = plateforme.calculerChemins(villeDepart, villeArrivee, k, utilisateur);

        List<ResultatTrajet> resultats = new ArrayList<>();

        for (Chemin chemin : chemins) {
            if (respecteTransport(chemin, transport)) {
                double prix = plateforme.calculerTotal(chemin, TypeCout.PRIX);
                double temps = plateforme.calculerTotal(chemin, TypeCout.TEMPS);
                double co2 = plateforme.calculerTotal(chemin, TypeCout.CO2);
                List<String> etapes = plateforme.extrairePointsInteret(chemin, villeDepart, villeArrivee);

                resultats.add(new ResultatTrajet(prix, temps, co2, etapes, chemin.poids()));
            }
        }

        if (resultats.isEmpty()) {
            throw new NoPathFound("Aucun trajet ne correspond aux critères demandés.");
        }

        return resultats;
    }

    private Voyageur creerVoyageurSelonCritere(String nom, TypeCout critere) {
        return switch (critere) {
            case PRIX -> new Voyageur(nom, 1.0, 0.0, 0.0);
            case TEMPS -> new Voyageur(nom, 0.0, 1.0, 0.0);
            case CO2 -> new Voyageur(nom, 0.0, 0.0, 1.0);
        };
    }

    private boolean respecteTransport(Chemin chemin, ModaliteTransport transport) {
        for (Connexion connexion : chemin.aretes()) {
            if (connexion instanceof Route route) {
                if (route.getModalite() != transport) {
                    return false;
                }
            }
        }
        return true;
    }

    private String formaterChemin(Plateforme plateforme,
                                  Chemin chemin,
                                  Lieu depart,
                                  Lieu arrivee,
                                  TypeCout critere) {

        double totalPrix = plateforme.calculerTotal(chemin, TypeCout.PRIX);
        double totalTemps = plateforme.calculerTotal(chemin, TypeCout.TEMPS);
        double totalCo2 = plateforme.calculerTotal(chemin, TypeCout.CO2);
        List<String> etapes = plateforme.extrairePointsInteret(chemin, depart, arrivee);

        StringBuilder sb = new StringBuilder();

        sb.append("Critère principal : ").append(critere).append("\n");
        sb.append("Score total : ").append(chemin.poids()).append("\n");
        sb.append("Prix total : ").append(totalPrix).append(" €\n");
        sb.append("Temps total : ").append(totalTemps).append(" min\n");
        sb.append("CO2 total : ").append(totalCo2).append(" kgCO2e\n");
        sb.append("Étapes :\n");

        for (String etape : etapes) {
            sb.append("- ").append(etape).append("\n");
        }

        return sb.toString();
    }

    /** Conteneur simple pour représenter un trajet calculé, utilisé par l'IHM. */
    public static class ResultatTrajet {
        public final double prix;
        public final double temps;
        public final double co2;
        public final List<String> etapes;
        public final double score;

        public ResultatTrajet(double prix, double temps, double co2, List<String> etapes, double score) {
            this.prix = prix;
            this.temps = temps;
            this.co2 = co2;
            this.etapes = etapes;
            this.score = score;
        }
    }
}