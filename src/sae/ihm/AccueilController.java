package sae.ihm;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class AccueilController {

    @FXML
    private TextField name;

    @FXML
    private void nextPage() throws IOException {
        String nom = name.getText().trim();
        if (nom.isEmpty()) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Recherche.fxml"));
        Parent root = loader.load();

        RechercheController controller = loader.getController();
        controller.setNomUtilisateur(nom);

        Stage stage = (Stage) name.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
