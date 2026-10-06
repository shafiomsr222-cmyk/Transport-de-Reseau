package sae.ihm;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import sae.service.VoyageService;

import java.io.IOException;
import java.util.List;

public class RechercheController {

    @FXML
    private MenuButton departMenu;

    @FXML
    private MenuButton arriveeMenu;

    @FXML
    private RadioButton radioTrain;

    @FXML
    private RadioButton radioBus;

    @FXML
    private RadioButton radioAvion;

    @FXML
    private CheckBox checkEmission;

    @FXML
    private CheckBox checkPrix;

    @FXML
    private CheckBox checkTemps;

    @FXML
    private Label nomLabel;

    @FXML
    private TextField dureeMaxField;

    @FXML
    private Label erreurLabel;

    private String nomUtilisateur;
    private ToggleGroup transportGroup;
    private final VoyageService voyageService = new VoyageService();

    @FXML
    private void initialize() {
        transportGroup = new ToggleGroup();
        radioTrain.setToggleGroup(transportGroup);
        radioBus.setToggleGroup(transportGroup);
        radioAvion.setToggleGroup(transportGroup);
        radioTrain.setSelected(true);

        departMenu.setText("Choisissez votre ville");
        arriveeMenu.setText("Choisissez votre ville");

        initialiserVillesDepuisCSV();
        initialiserChoixCritere();
    }

    private void initialiserVillesDepuisCSV() {
        try {
            List<String> villes = voyageService.chargerVilles();

            departMenu.getItems().clear();
            arriveeMenu.getItems().clear();

            for (String ville : villes) {
                MenuItem itemDepart = new MenuItem(ville);
                itemDepart.setOnAction(e -> departMenu.setText(ville));
                departMenu.getItems().add(itemDepart);

                MenuItem itemArrivee = new MenuItem(ville);
                itemArrivee.setOnAction(e -> arriveeMenu.setText(ville));
                arriveeMenu.getItems().add(itemArrivee);
            }
        } catch (Exception e) {
            departMenu.setText("Erreur CSV");
            arriveeMenu.setText("Erreur CSV");
            e.printStackTrace();
        }
    }

    private void initialiserChoixCritere() {
        checkEmission.setSelected(true);

        checkPrix.setOnAction(e -> {
            if (checkPrix.isSelected()) {
                checkEmission.setSelected(false);
                checkTemps.setSelected(false);
            } else if (!checkEmission.isSelected() && !checkTemps.isSelected()) {
                checkEmission.setSelected(true);
            }
        });

        checkEmission.setOnAction(e -> {
            if (checkEmission.isSelected()) {
                checkPrix.setSelected(false);
                checkTemps.setSelected(false);
            } else if (!checkPrix.isSelected() && !checkTemps.isSelected()) {
                checkEmission.setSelected(true);
            }
        });

        checkTemps.setOnAction(e -> {
            if (checkTemps.isSelected()) {
                checkEmission.setSelected(false);
                checkPrix.setSelected(false);
            } else if (!checkEmission.isSelected() && !checkPrix.isSelected()) {
                checkEmission.setSelected(true);
            }
        });
    }

    public void setNomUtilisateur(String nom) {
        this.nomUtilisateur = nom;
        if (nomLabel != null) {
            nomLabel.setText("Bonjour " + nom + " !");
        }
    }

    @FXML
    private void allerHistorique() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Historique.fxml"));
        Parent root = loader.load();

        HistoriqueController controller = loader.getController();
        controller.setNomUtilisateur(nomUtilisateur);

        Stage stage = (Stage) departMenu.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void nextPage() {
        erreurLabel.setText("");

        String depart = departMenu.getText();
        String arrivee = arriveeMenu.getText();

        if (depart == null || arrivee == null) return;
        if (depart.equals("Choisissez votre ville") || arrivee.equals("Choisissez votre ville")) {
            erreurLabel.setText("Veuillez sélectionner un départ et une arrivée.");
            return;
        }
        if (depart.equals("Erreur CSV") || arrivee.equals("Erreur CSV")) return;
        if (depart.equals(arrivee)) {
            erreurLabel.setText("Le départ et l'arrivée doivent être différents.");
            return;
        }

        String transport;
        if (radioTrain.isSelected()) {
            transport = "TRAIN";
        } else if (radioBus.isSelected()) {
            transport = "BUS";
        } else {
            transport = "AVION";
        }

        String critere;
        if (checkEmission.isSelected()) {
            critere = "CO2";
        } else if (checkPrix.isSelected()) {
            critere = "PRIX";
        } else {
            critere = "TEMPS";
        }

        Double dureeMax = null;
        String dureeTexte = dureeMaxField.getText();
        if (dureeTexte != null && !dureeTexte.isBlank()) {
            try {
                dureeMax = Double.parseDouble(dureeTexte.trim());
                if (dureeMax < 0) {
                    erreurLabel.setText("La durée maximale doit être positive.");
                    return;
                }
            } catch (NumberFormatException e) {
                erreurLabel.setText("La durée maximale doit être un nombre.");
                return;
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ResultatRecherche.fxml"));
            Parent root = loader.load();

            ResultatRechercheController controller = loader.getController();
            controller.setDonnees(nomUtilisateur, depart, arrivee, transport, critere, dureeMax);

            Stage stage = (Stage) departMenu.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            erreurLabel.setText("Erreur lors du calcul du trajet.");
        }
    }
}