package sae.ihm;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sae.poo.HistoriqueVoyage;
import sae.poo.Voyage;

import java.io.IOException;
import java.util.List;

public class HistoriqueController {

    @FXML private Label villeDepart1;
    @FXML private Label villeArrivee1;
    @FXML private Label date1;
    @FXML private Label critere1;

    @FXML private Label villeDepart2;
    @FXML private Label villeArrivee2;
    @FXML private Label date2;
    @FXML private Label critere2;

    @FXML private Label moyennePrixLabel;
    @FXML private Label moyenneTempsLabel;
    @FXML private Label moyenneCo2Label;
    @FXML private Label nomLabel;
    @FXML private Label erreurLabel;

    private String nomUtilisateur;

    public void setNomUtilisateur(String nom) {
        this.nomUtilisateur = nom;
        nomLabel.setText("Historique de " + nom);
        chargerHistorique();
    }

    private void chargerHistorique() {
        try {
            String fichierHistorique = getNomFichierHistorique(nomUtilisateur);
            HistoriqueVoyage historique = HistoriqueVoyage.charger(fichierHistorique);
            List<Voyage> voyages = historique.getVoyages();

            if (voyages.isEmpty()) {
                erreurLabel.setText("Aucun voyage enregistré.");
                viderBloc1();
                viderBloc2();
                moyennePrixLabel.setText("Prix moyen : -");
                moyenneTempsLabel.setText("Temps moyen : -");
                moyenneCo2Label.setText("CO2 moyen : -");
                return;
            }

            erreurLabel.setText("");

            Voyage v1 = voyages.get(voyages.size() - 1);
            villeDepart1.setText(v1.getVilleDepart());
            villeArrivee1.setText(v1.getVilleArrivee());
            date1.setText(v1.getDate().toString());
            critere1.setText(String.format("%.2f € | %.2f min | %.2f kg", v1.getPrix(), v1.getTemps(), v1.getCo2()));

            if (voyages.size() > 1) {
                Voyage v2 = voyages.get(voyages.size() - 2);
                villeDepart2.setText(v2.getVilleDepart());
                villeArrivee2.setText(v2.getVilleArrivee());
                date2.setText(v2.getDate().toString());
                critere2.setText(String.format("%.2f € | %.2f min | %.2f kg", v2.getPrix(), v2.getTemps(), v2.getCo2()));
            } else {
                viderBloc2();
            }

            double moyennePrix = voyages.stream().mapToDouble(Voyage::getPrix).average().orElse(0);
            double moyenneTemps = voyages.stream().mapToDouble(Voyage::getTemps).average().orElse(0);
            double moyenneCo2 = voyages.stream().mapToDouble(Voyage::getCo2).average().orElse(0);

            moyennePrixLabel.setText(String.format("Prix moyen : %.2f €", moyennePrix));
            moyenneTempsLabel.setText(String.format("Temps moyen : %.2f min", moyenneTemps));
            moyenneCo2Label.setText(String.format("CO2 moyen : %.2f kg", moyenneCo2));

        } catch (Exception e) {
            erreurLabel.setText("Aucun historique disponible.");
            viderBloc1();
            viderBloc2();
            moyennePrixLabel.setText("Prix moyen : -");
            moyenneTempsLabel.setText("Temps moyen : -");
            moyenneCo2Label.setText("CO2 moyen : -");
        }
    }

    private String getNomFichierHistorique(String nom) {
        String safeName = nom.replaceAll("[^a-zA-Z0-9_-]", "_");
        return "historique_" + safeName + ".dat";
    }

    private void viderBloc1() {
        villeDepart1.setText("-");
        villeArrivee1.setText("-");
        date1.setText("-");
        critere1.setText("-");
    }

    private void viderBloc2() {
        villeDepart2.setText("-");
        villeArrivee2.setText("-");
        date2.setText("-");
        critere2.setText("-");
    }

    @FXML
    private void retour() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Recherche.fxml"));
        Parent root = loader.load();

        RechercheController controller = loader.getController();
        controller.setNomUtilisateur(nomUtilisateur);

        Stage stage = (Stage) villeDepart1.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}