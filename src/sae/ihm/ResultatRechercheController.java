package sae.ihm;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sae.poo.Voyage;
import sae.poo.Voyageur;
import sae.service.VoyageService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ResultatRechercheController {

    @FXML
    private Label villeDepart;

    @FXML
    private Label villeArrivee;

    @FXML
    private Label critereLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private VBox resultatsBox;

    private String nomUtilisateur;
    private String depart;
    private String arrivee;
    private String transport;
    private String critere;
    private final VoyageService voyageService = new VoyageService();

    public void setDonnees(String nom, String depart, String arrivee, String transport, String critere, Double dureeMax) {
        this.nomUtilisateur = nom;
        this.depart = depart;
        this.arrivee = arrivee;
        this.transport = transport;
        this.critere = critere;

        if (villeDepart != null) villeDepart.setText(depart);
        if (villeArrivee != null) villeArrivee.setText(arrivee);
        if (critereLabel != null) {
            critereLabel.setText("Transport : " + libelleTransport(transport) + "  •  Critère : " + libelleCritere(critere));
        }
        if (dateLabel != null) dateLabel.setText(LocalDate.now().toString());

        resultatsBox.getChildren().clear();

        try {
            List<VoyageService.ResultatTrajet> resultats =
                    voyageService.rechercherDetaille(nom, depart, arrivee, transport, critere, 5, dureeMax);

            // Trouver l'indice du trajet le plus écologique (CO2 minimal)
            int indexEcolo = 0;
            double minCo2 = Double.MAX_VALUE;
            for (int i = 0; i < resultats.size(); i++) {
                if (resultats.get(i).co2 < minCo2) {
                    minCo2 = resultats.get(i).co2;
                    indexEcolo = i;
                }
            }

            for (int i = 0; i < resultats.size(); i++) {
                VoyageService.ResultatTrajet r = resultats.get(i);
                boolean estEcolo = (i == indexEcolo);
                resultatsBox.getChildren().add(creerCarteTrajet(i + 1, r, estEcolo));
            }

            // Enregistrement du premier (meilleur selon le critère) dans l'historique
            if (!resultats.isEmpty()) {
                enregistrerDansHistorique(nom, depart, arrivee, resultats.get(0));
            }

        } catch (Exception e) {
            Label erreur = new Label("Erreur : " + e.getMessage());
            erreur.setStyle("-fx-text-fill: #1A1A1A; -fx-font-size: 14px; -fx-padding: 20;");
            resultatsBox.getChildren().add(erreur);
        }
    }

    private VBox creerCarteTrajet(int rang, VoyageService.ResultatTrajet r, boolean estEcolo) {
        VBox carte = new VBox(8);
        carte.setStyle(
                "-fx-background-color: white; -fx-background-radius: 14; -fx-padding: 18;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8, 0, 0, 2);"
                + (estEcolo ? "-fx-border-color: #1A1A1A; -fx-border-width: 1.5; -fx-border-radius: 14;" : "")
        );
        carte.setCursor(Cursor.HAND);

        HBox header = new HBox(10);
        Label titre = new Label("Trajet " + rang);
        titre.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1A1A1A;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().add(titre);
        header.getChildren().add(spacer);

        if (estEcolo) {
            Label badge = new Label("🌱 Le plus écologique");
            badge.setStyle(
                    "-fx-background-color: #1A1A1A; -fx-text-fill: white; -fx-font-weight: bold;"
                    + "-fx-font-size: 11px; -fx-background-radius: 20; -fx-padding: 4 12;"
            );
            header.getChildren().add(badge);
        }

        Label chevron = new Label("›");
        chevron.setStyle("-fx-font-size: 18px; -fx-text-fill: #BBBBBB; -fx-padding: 0 0 0 6;");
        header.getChildren().add(chevron);

        carte.getChildren().add(header);

        // Ligne de coûts
        HBox couts = new HBox(24);
        couts.getChildren().add(creerCout(String.format("%.2f €", r.prix), "Prix"));
        couts.getChildren().add(creerCout(String.format("%.0f min", r.temps), "Temps"));
        couts.getChildren().add(creerCout(String.format("%.1f kg CO2e", r.co2), "Émissions"));
        carte.getChildren().add(couts);

        // Aperçu de l'itinéraire (résumé court)
        if (!r.etapes.isEmpty()) {
            Label apercu = new Label(String.join("  →  ", r.etapes));
            apercu.setStyle("-fx-font-size: 12px; -fx-text-fill: #999999;");
            apercu.setWrapText(true);
            carte.getChildren().add(apercu);
        }

        carte.setOnMouseClicked(e -> ouvrirDetails(r, estEcolo));

        return carte;
    }

    private VBox creerCout(String valeur, String label) {
        VBox box = new VBox(2);
        Label valeurLabel = new Label(valeur);
        valeurLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1A1A1A;");
        Label nomLabel = new Label(label);
        nomLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #999999;");
        box.getChildren().addAll(valeurLabel, nomLabel);
        return box;
    }

    private void ouvrirDetails(VoyageService.ResultatTrajet trajet, boolean estEcolo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailsVoyage.fxml"));
            Parent root = loader.load();

            DetailsVoyageController controller = loader.getController();
            controller.setDonnees(nomUtilisateur, depart, arrivee, transport, critere, trajet, estEcolo);

            Stage stage = (Stage) resultatsBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String libelleTransport(String transport) {
        return switch (transport) {
            case "TRAIN" -> "Train";
            case "BUS" -> "Bus";
            case "AVION" -> "Avion";
            default -> transport;
        };
    }

    private String libelleCritere(String critere) {
        return switch (critere) {
            case "CO2" -> "Émissions";
            case "PRIX" -> "Prix";
            case "TEMPS" -> "Temps";
            default -> critere;
        };
    }

    /**
     * Enregistre le meilleur trajet trouvé dans l'historique sérialisé de l'utilisateur,
     * conformément à la V3 du sujet (sérialisation binaire de l'historique).
     */
    private void enregistrerDansHistorique(String nom, String depart, String arrivee, VoyageService.ResultatTrajet r) {
        try {
            String fichier = "historique_" + nom.replaceAll("[^a-zA-Z0-9_-]", "_") + ".dat";

            Voyageur voyageur = new Voyageur(nom, 1.0, 0.0, 0.0);
            try {
                voyageur.chargerHistorique(fichier);
            } catch (Exception ignored) {
                // Pas d'historique existant : on démarre à zéro
            }

            Voyage voyage = new Voyage(depart, arrivee, r.prix, r.temps, r.co2, LocalDate.now(), r.etapes);
            voyageur.ajouterVoyage(voyage);
            voyageur.sauvegarder(fichier);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void retourRecherche() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Recherche.fxml"));
        Parent root = loader.load();

        RechercheController controller = loader.getController();
        controller.setNomUtilisateur(nomUtilisateur);

        Stage stage = (Stage) resultatsBox.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void retourAccueil() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("accueil.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) resultatsBox.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}