package sae.poo;

import fr.ulille.but.sae_s2_2026.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe représente la plateforme de transport.
 * Son rôle est de :
 * - lire les fichiers CSV,
 * - créer les villes, stations, routes et correspondances,
 * - construire le graphe,
 * - chercher et afficher les meilleurs trajets.
 */
public class Plateforme {

    private MultiGrapheOrienteValue graphe;
    private ArrayList<Ville> villes;
    private ArrayList<Station> stations;
    private ArrayList<Route> routes;
    private ArrayList<Correspondance> correspondances;

    public Plateforme(String pathGraphe, String pathCorrespondances, Voyageur user) {
        this.graphe = new MultiGrapheOrienteValue();
        this.villes = new ArrayList<>();
        this.stations = new ArrayList<>();
        this.routes = new ArrayList<>();
        this.correspondances = new ArrayList<>();

        ArrayList<String[]> donneesGraphe = lireCsv(pathGraphe, ';');
        ArrayList<String[]> donneesCorrespondances = lireCsv(pathCorrespondances, ';');

        validerDonnees(donneesGraphe);
        validerDonnees(donneesCorrespondances);

        chargerVilles(donneesGraphe);
        chargerStations(donneesGraphe);
        chargerRoutes(donneesGraphe);
        chargerCorrespondances(donneesCorrespondances);

        construireGraphe(user);
    }

    public ArrayList<String[]> lireCsv(String path, char separateur) {
        ArrayList<String[]> resultat = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String ligne = br.readLine();

            if (ligne == null) {
                throw new EmptyCSV("Le fichier CSV " + new File(path).getPath() + " est vide.", new File(path));
            }

            while (ligne != null) {
                resultat.add(ligne.split(Character.toString(separateur)));
                ligne = br.readLine();
            }

        } catch (IOException e) {
            throw new InvalidCSVData("Erreur de lecture du fichier : " + path, -1, "Lecture impossible");
        }

        return resultat;
    }

    public void chargerVilles(ArrayList<String[]> donnees) {
        for (String[] ligne : donnees) {
            Ville villeDepart = new Ville(ligne[0]);
            Ville villeArrivee = new Ville(ligne[1]);

            if (!this.villes.contains(villeDepart)) {
                this.villes.add(villeDepart);
            }

            if (!this.villes.contains(villeArrivee)) {
                this.villes.add(villeArrivee);
            }
        }
    }

    public void chargerStations(ArrayList<String[]> donnees) {
        for (String[] ligne : donnees) {
            Ville villeDepart = trouverVilleParNom(ligne[0]);
            Ville villeArrivee = trouverVilleParNom(ligne[1]);
            ModaliteTransport modalite = ModaliteTransport.valueOf(ligne[2].toUpperCase());

            Station stationDepart = new Station(villeDepart, modalite);
            Station stationArrivee = new Station(villeArrivee, modalite);

            if (!this.stations.contains(stationDepart)) {
                this.stations.add(stationDepart);
            }

            if (!this.stations.contains(stationArrivee)) {
                this.stations.add(stationArrivee);
            }
        }
    }

    public void chargerRoutes(ArrayList<String[]> donnees) {
        for (String[] ligne : donnees) {
            String nomVilleDepart = ligne[0];
            String nomVilleArrivee = ligne[1];
            ModaliteTransport modalite = ModaliteTransport.valueOf(ligne[2].toUpperCase());

            Station stationDepart = trouverStation(nomVilleDepart, modalite);
            Station stationArrivee = trouverStation(nomVilleArrivee, modalite);

            if (stationDepart == null) {
                throw new UnknownCity(
                        "Route de " + nomVilleDepart + " vers " + nomVilleArrivee
                                + " : station de départ inconnue.",
                        nomVilleDepart);
            }

            if (stationArrivee == null) {
                throw new UnknownCity(
                        "Route de " + nomVilleDepart + " vers " + nomVilleArrivee
                                + " : station d'arrivée inconnue.",
                        nomVilleArrivee);
            }

            double prix = Double.parseDouble(ligne[3]);
            double co2 = Double.parseDouble(ligne[4]);
            double temps = Double.parseDouble(ligne[5]);

            Route route = new Route(stationDepart, stationArrivee, modalite, prix, co2, temps);

            if (!this.routes.contains(route)) {
                this.routes.add(route);
            }
        }
    }

    public void chargerCorrespondances(ArrayList<String[]> donnees) {
        for (String[] ligne : donnees) {
            String nomVille = ligne[0];
            ModaliteTransport modaliteDepart = ModaliteTransport.valueOf(ligne[1].toUpperCase());
            ModaliteTransport modaliteArrivee = ModaliteTransport.valueOf(ligne[2].toUpperCase());

            Station stationDepart = trouverStation(nomVille, modaliteDepart);
            Station stationArrivee = trouverStation(nomVille, modaliteArrivee);

            if (stationDepart != null && stationArrivee != null) {
                double temps = Double.parseDouble(ligne[3]);
                double co2 = Double.parseDouble(ligne[4]);
                double prix = Double.parseDouble(ligne[5]);

                this.correspondances.add(
                        new Correspondance(stationDepart, stationArrivee, prix, co2, temps));
            }
        }
    }

    public void construireGraphe(Voyageur user) {
        for (Ville ville : this.villes) {
            this.graphe.ajouterSommet(ville);
        }

        for (Station station : this.stations) {
            this.graphe.ajouterSommet(station);
        }

        for (Route route : this.routes) {
            this.graphe.ajouterArete(route, getPoidsConnexion(route, user));
        }

        for (Correspondance correspondance : this.correspondances) {
            this.graphe.ajouterArete(correspondance, getPoidsConnexion(correspondance, user));
        }
    }

    public double getPoidsConnexion(Connexion connexion, Voyageur user) {
        double prix = getCoutSelonCritere(connexion, TypeCout.PRIX);
        double temps = getCoutSelonCritere(connexion, TypeCout.TEMPS);
        double co2 = getCoutSelonCritere(connexion, TypeCout.CO2);

        return prix * user.getPoids(TypeCout.PRIX)
                + temps * user.getPoids(TypeCout.TEMPS)
                + co2 * user.getPoids(TypeCout.CO2);
    }

    public double getCoutSelonCritere(Connexion connexion, TypeCout critere) {
        if (connexion instanceof Route route) {
            return switch (critere) {
                case PRIX -> route.getCoutPrix();
                case TEMPS -> route.getCoutTemps();
                case CO2 -> route.getCoutCo2();
            };
        } else if (connexion instanceof Correspondance correspondance) {
            return switch (critere) {
                case PRIX -> correspondance.getCoutPrix();
                case TEMPS -> correspondance.getCoutTemps();
                case CO2 -> correspondance.getCoutCo2();
            };
        }
        return 0.0;
    }

    public boolean respecteContraintes(Chemin chemin, Voyageur user) {
        double totalPrix = 0.0;
        double totalTemps = 0.0;
        double totalCo2 = 0.0;

        for (Connexion connexion : chemin.aretes()) {
            totalPrix += getCoutSelonCritere(connexion, TypeCout.PRIX);
            totalTemps += getCoutSelonCritere(connexion, TypeCout.TEMPS);
            totalCo2 += getCoutSelonCritere(connexion, TypeCout.CO2);
        }

        if (user.getMaxPrix() != null && totalPrix > user.getMaxPrix()) {
            return false;
        }

        if (user.getMaxTemps() != null && totalTemps > user.getMaxTemps()) {
            return false;
        }

        if (user.getMaxCo2() != null && totalCo2 > user.getMaxCo2()) {
            return false;
        }

        return true;
    }

    public Ville trouverVilleParNom(String nomVille) {
        for (Ville ville : this.villes) {
            if (ville.getName().equals(nomVille)) {
                return ville;
            }
        }
        return null;
    }

    public Station trouverStation(String nomVille, ModaliteTransport modalite) {
        for (Station station : this.stations) {
            if (station.getVille().getName().equals(nomVille)
                    && station.getModaliteTransport() == modalite) {
                return station;
            }
        }
        return null;
    }

    public ArrayList<Station> trouverStationsDuneVille(Lieu ville) {
        ArrayList<Station> resultat = new ArrayList<>();

        for (Station station : this.stations) {
            if (station.getVille().equals(ville)) {
                resultat.add(station);
            }
        }

        return resultat;
    }

    public String upperFirstLettre(String texte) {
        return texte.substring(0, 1).toUpperCase() + texte.substring(1).toLowerCase();
    }

    public void userChemin(Lieu depart, Lieu arrive, int k, String libelleCritere, Voyageur user)
            throws NoPathFound, UnknownCity {

        if (!this.villes.contains(depart)) {
            throw new UnknownCity("Ville de départ \"" + depart.toString() + "\" n'est pas sur le réseau.",
                    depart.toString());
        }

        if (!this.villes.contains(arrive)) {
            throw new UnknownCity("Ville d'arrivée \"" + arrive.toString() + "\" n'est pas sur le réseau.",
                    arrive.toString());
        }

        ArrayList<LiaisonVirtuelle> liaisonsTemporaires = new ArrayList<>();

        for (Station s : this.stations) {
            if (s.getVille().equals(depart)) {
                LiaisonVirtuelle entree = new LiaisonVirtuelle(depart, s, null);
                this.graphe.ajouterArete(entree, 0.0);
                liaisonsTemporaires.add(entree);
            } else if (s.getVille().equals(arrive)) {
                LiaisonVirtuelle sortie = new LiaisonVirtuelle(s, arrive, null);
                this.graphe.ajouterArete(sortie, 0.0);
                liaisonsTemporaires.add(sortie);
            }
        }

        ArrayList<Chemin> listChemin = new ArrayList<>(AlgorithmeKPCC.kpcc(this.graphe, depart, arrive, k));

        if (k != 0 && listChemin.isEmpty()) {
            throw new NoPathFound("Aucun chemin n'est disponible pour aller de " + depart.toString() + " vers "
                    + arrive.toString() + ".");
        }

        ArrayList<Chemin> cheminsFiltres = new ArrayList<>();
        for (Chemin chemin : listChemin) {
            if (respecteContraintes(chemin, user)) {
                cheminsFiltres.add(chemin);
            }
        }

        if (k != 0 && cheminsFiltres.isEmpty()) {
            throw new NoPathFound("Aucun trajet ne respecte les contraintes de l'utilisateur.");
        }

        System.out.println(
                "Les " + cheminsFiltres.size() + " trajets recommandés de " + depart + " à " + arrive + " sont :\n");

        for (int i = 0; i < cheminsFiltres.size(); i++) {
            Chemin chemin = cheminsFiltres.get(i);
            System.out.println((i + 1) + " | " + libelleCritere + " total : " + (int) chemin.poids());

            double totalPrix = 0.0;
            double totalTemps = 0.0;
            double totalCo2 = 0.0;

            for (Connexion connexion : chemin.aretes()) {
                totalPrix += getCoutSelonCritere(connexion, TypeCout.PRIX);
                totalTemps += getCoutSelonCritere(connexion, TypeCout.TEMPS);
                totalCo2 += getCoutSelonCritere(connexion, TypeCout.CO2);
            }

            System.out.println("   Prix total : " + totalPrix + " €");
            System.out.println("   Temps total : " + totalTemps + " min");
            System.out.println("   CO2 total : " + totalCo2 + " kgCO2e");

            ModaliteTransport premierTransport = null;
            for (Connexion arete : chemin.aretes()) {
                if (arete instanceof Route) {
                    premierTransport = arete.getModalite();
                    break;
                }
            }

            if (premierTransport != null) {
                System.out.println(" Départ de " + depart + " en " + upperFirstLettre(premierTransport.name()));
            }

            for (Connexion arete : chemin.aretes()) {
                if (arete instanceof Correspondance) {
                    Station stationChangement = (Station) arete.getDepart();
                    System.out.println(" Correspondance à " + stationChangement.getVille().getName()
                            + " (Changement pour un " + upperFirstLettre(arete.getModalite().name()) + ")");
                }
            }

            System.out.println(" Arrivée à " + arrive + "\n");
        }
    }

    public ArrayList<Ville> getVilles() {
        return this.villes;
    }

    public void verifierVilleConnue(Lieu ville) throws UnknownCity {
        if (!this.villes.contains(ville)) {
            throw new UnknownCity(
                    "La ville \"" + ville.toString() + "\" n'est pas dans la liste des villes disponibles.",
                    ville.toString());
        }
    }

    public ArrayList<Chemin> calculerChemins(Lieu depart, Lieu arrive, int k, Voyageur user)
            throws NoPathFound, UnknownCity {

        if (!this.villes.contains(depart)) {
            throw new UnknownCity("Ville de départ \"" + depart + "\" n'est pas sur le réseau.", depart.toString());
        }

        if (!this.villes.contains(arrive)) {
            throw new UnknownCity("Ville d'arrivée \"" + arrive + "\" n'est pas sur le réseau.", arrive.toString());
        }

        ArrayList<LiaisonVirtuelle> liaisonsTemporaires = new ArrayList<>();

        for (Station s : this.stations) {
            if (s.getVille().equals(depart)) {
                LiaisonVirtuelle entree = new LiaisonVirtuelle(depart, s, null);
                this.graphe.ajouterArete(entree, 0.0);
                liaisonsTemporaires.add(entree);
            } else if (s.getVille().equals(arrive)) {
                LiaisonVirtuelle sortie = new LiaisonVirtuelle(s, arrive, null);
                this.graphe.ajouterArete(sortie, 0.0);
                liaisonsTemporaires.add(sortie);
            }
        }

        ArrayList<Chemin> listChemin = new ArrayList<>(AlgorithmeKPCC.kpcc(this.graphe, depart, arrive, k));

        if (k != 0 && listChemin.isEmpty()) {
            throw new NoPathFound("Aucun chemin n'est disponible pour aller de " + depart + " vers " + arrive + ".");
        }

        ArrayList<Chemin> cheminsFiltres = new ArrayList<>();
        for (Chemin chemin : listChemin) {
            if (respecteContraintes(chemin, user)) {
                cheminsFiltres.add(chemin);
            }
        }

        if (k != 0 && cheminsFiltres.isEmpty()) {
            throw new NoPathFound("Aucun trajet ne respecte les contraintes de l'utilisateur.");
        }

        return cheminsFiltres;
    }

    public double calculerTotal(Chemin chemin, TypeCout typeCout) {
        double total = 0.0;

        for (Connexion connexion : chemin.aretes()) {
            total += getCoutSelonCritere(connexion, typeCout);
        }

        return total;
    }

    public List<String> extrairePointsInteret(Chemin chemin, Lieu depart, Lieu arrive) {
        List<String> etapes = new ArrayList<>();
        etapes.add("Départ : " + depart);

        for (Connexion connexion : chemin.aretes()) {
            if (connexion instanceof Correspondance correspondance) {
                Station stationDepart = (Station) correspondance.getDepart();
                Station stationArrivee = (Station) correspondance.getArrivee();

                etapes.add(
                        "Correspondance à " + stationDepart.getVille().getName()
                                + " (" + stationDepart.getModaliteTransport()
                                + " -> " + stationArrivee.getModaliteTransport() + ")");
            }
        }

        etapes.add("Arrivée : " + arrive);
        return etapes;
    }

    public void validerDonnees(ArrayList<String[]> donnees) throws InvalidCSVData {
        for (int i = 0; i < donnees.size(); i++) {
            String[] ligne = donnees.get(i);
            int numLigne = i + 1;

            if (ligne.length != 6) {
                throw new InvalidCSVData(
                        "Ligne " + numLigne + " invalide : " + ligne.length + " champ(s) trouvé(s) au lieu de 6.",
                        numLigne,
                        "Nombre de champs incorrect");
            }

            double prix;
            double co2;
            double duree;

            try {
                prix = Double.parseDouble(ligne[3]);
            } catch (NumberFormatException e) {
                throw new InvalidCSVData(
                        "Ligne " + numLigne + " : le prix \"" + ligne[3] + "\" n'est pas un nombre.",
                        numLigne,
                        "Prix non numérique");
            }

            try {
                co2 = Double.parseDouble(ligne[4]);
            } catch (NumberFormatException e) {
                throw new InvalidCSVData(
                        "Ligne " + numLigne + " : le CO2 \"" + ligne[4] + "\" n'est pas un nombre.",
                        numLigne,
                        "CO2 non numérique");
            }

            try {
                duree = Double.parseDouble(ligne[5]);
            } catch (NumberFormatException e) {
                throw new InvalidCSVData(
                        "Ligne " + numLigne + " : la durée \"" + ligne[5] + "\" n'est pas un nombre.",
                        numLigne,
                        "Durée non numérique");
            }

            if (prix < 0) {
                throw new InvalidCSVData(
                        "Ligne " + numLigne + " : le prix (" + prix + ") est négatif.",
                        numLigne,
                        "Prix négatif");
            }

            if (co2 < 0) {
                throw new InvalidCSVData(
                        "Ligne " + numLigne + " : le CO2 (" + co2 + ") est négatif.",
                        numLigne,
                        "CO2 négatif");
            }

            if (duree < 0) {
                throw new InvalidCSVData(
                        "Ligne " + numLigne + " : la durée (" + duree + ") est négative.",
                        numLigne,
                        "Durée négative");
            }
        }
    }
}