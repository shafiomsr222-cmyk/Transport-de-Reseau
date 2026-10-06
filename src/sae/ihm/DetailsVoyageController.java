package sae.ihm;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sae.service.VoyageService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class DetailsVoyageController {

    @FXML private Label villeDepart;
    @FXML private Label villeArrivee;
    @FXML private Label critereLabel;
    @FXML private Label dateLabel;
    @FXML private Label badgeLabel;
    @FXML private Label prixLabel;
    @FXML private Label tempsLabel;
    @FXML private Label co2Label;
    @FXML private VBox etapesBox;

    private String nomUtilisateur;

    public void setDonnees(String nom, String depart, String arrivee, String transport, String critere,
                            VoyageService.ResultatTrajet trajet, boolean estEcolo) {
        this.nomUtilisateur = nom;

        if (villeDepart != null) villeDepart.setText(depart);
        if (villeArrivee != null) villeArrivee.setText(arrivee);

        if (critereLabel != null) {
            critereLabel.setText("Transport : " + libelleTransport(transport) + "  •  Critère optimisé : " + libelleCritere(critere));
        }

        if (dateLabel != null) dateLabel.setText(LocalDate.now().toString());

        if (badgeLabel != null) {
            badgeLabel.setText(estEcolo ? "🌱 TRAJET LE PLUS ÉCOLOGIQUE" : "DÉTAIL DU TRAJET");
        }

        if (prixLabel != null) prixLabel.setText(String.format("%.2f €", trajet.prix));
        if (tempsLabel != null) tempsLabel.setText(String.format("%.0f min", trajet.temps));
        if (co2Label != null) co2Label.setText(String.format("%.1f kg", trajet.co2));

        afficherEtapes(trajet.etapes);
    }

    private void afficherEtapes(List<String> etapes) {
        etapesBox.getChildren().clear();

        for (int i = 0; i < etapes.size(); i++) {
            boolean dernier = (i == etapes.size() - 1);

            HBox ligne = new HBox(14);
            ligne.setStyle("-fx-padding: 0 0 " + (dernier ? "0" : "16") + " 0;");

            // Pastille numérotée
            Label pastille = new Label(String.valueOf(i + 1));
            pastille.setMinSize(28, 28);
            pastille.setMaxSize(28, 28);
            pastille.setAlignment(javafx.geometry.Pos.CENTER);
            pastille.setStyle(
                    "-fx-background-color: " + (dernier || i == 0 ? "#1A1A1A" : "#EFEFEF") + ";"
                    + "-fx-text-fill: " + (dernier || i == 0 ? "white" : "#1A1A1A") + ";"
                    + "-fx-background-radius: 14; -fx-font-size: 12px; -fx-font-weight: bold;"
            );

            VBox texteBox = new VBox(2);
            Label etapeLabel = new Label(etapes.get(i));
            etapeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1A1A1A; -fx-font-weight: " + (i == 0 || dernier ? "bold" : "normal") + ";");
            etapeLabel.setWrapText(true);
            texteBox.getChildren().add(etapeLabel);

            ligne.getChildren().addAll(pastille, texteBox);
            etapesBox.getChildren().add(ligne);

            // Trait de liaison vertical entre étapes (loi de Gestalt : continuité)
            if (!dernier) {
                Region trait = new Region();
                trait.setMinSize(2, 16);
                trait.setMaxSize(2, 16);
                trait.setStyle("-fx-background-color: #EFEFEF;");
                HBox traitBox = new HBox();
                traitBox.setStyle("-fx-padding: 0 0 0 13;");
                traitBox.getChildren().add(trait);
                etapesBox.getChildren().add(traitBox);
            }
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

    @FXML
    private void retourResultats() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Recherche.fxml"));
        Parent root = loader.load();

        RechercheController controller = loader.getController();
        controller.setNomUtilisateur(nomUtilisateur);

        Stage stage = (Stage) etapesBox.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}