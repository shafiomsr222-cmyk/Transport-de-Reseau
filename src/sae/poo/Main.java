package sae.poo;

import fr.ulille.but.sae_s2_2026.Lieu;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // ─── Création du voyageur (Version 3 : poids multi-critères) ──────────
        // Poids : PRIX 50%, TEMPS 30%, CO2 20%
        Voyageur user = new Voyageur("Alice", 0.5, 0.3, 0.2);

        // Contraintes maximales (Version 1 & 2)
        user.setMaxPrix(200.0);
        user.setMaxTemps(1000.0);
        user.setMaxCo2(300.0);

        // ─── Chargement de l'historique existant (Version 3 : sérialisation) ──
        try {
            user.chargerHistorique("historique_alice.dat");
            System.out.println("Historique chargé :\n" + user.getHistoriqueVoyage());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Aucun historique existant, démarrage à zéro.");
        }

        // ─── Création de la plateforme depuis les fichiers CSV ─────────────────
        Plateforme plateforme;
        try {
            plateforme = new Plateforme(
                    "ressources" + System.getProperty("file.separator") + "Poo-graphe.csv",
                    "ressources" + System.getProperty("file.separator") + "Poo-correspondances.csv",
                    user
            );
        } catch (InvalidCSVData e) {
            System.out.println("Erreur dans le fichier CSV : " + e.getRaison());
            System.out.println(e.getMessage());
            System.exit(-1);
            return;
        } catch (EmptyCSV e) {
            e.printStackTrace();
            System.exit(-1);
            return;
        }

        // ─── Recherche des meilleurs trajets ──────────────────────────────────
        Lieu depart  = new Ville("Paris");
        Lieu arrivee = new Ville("Marseille");

        try {
            plateforme.userChemin(depart, arrivee, 10, "Score combiné", user);
        } catch (NoPathFound e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        } catch (UnknownCity e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        // ─── Enregistrement du voyage dans l'historique ───────────────────────
        Voyage exempleVoyage = new Voyage(
                "Paris", "Marseille",
                120.0, 240.0, 15.0,
                LocalDate.now(),
                List.of("Paris-TRAIN", "Lyon-TRAIN", "Marseille-TRAIN")
        );
        user.ajouterVoyage(exempleVoyage);

        // Affichage de l'évolution du prix sur tous les voyages enregistrés
        System.out.println("\nÉvolution du coût PRIX : "
                + user.getHistoriqueVoyage().evolutionCout(TypeCout.PRIX));

        // ─── Sauvegarde de l'historique mis à jour ────────────────────────────
        try {
            user.sauvegarder("historique_alice.dat");
            System.out.println("Historique sauvegardé.");
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }
}
