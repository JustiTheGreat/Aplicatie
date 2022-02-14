package TotPanaLaAutentificare;

import MyConnection.Utillity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Alegere extends Utillity implements Initializable{
    @FXML
    private ChoiceBox<String> series;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    private void loadData(){
        String s[]={"Client","Centru de adoptie", "Petshop"};
        series.getItems().addAll(s);
    }

    public void selectare(javafx.event.ActionEvent actionEvent) {
        try {
            String alegere = series.getValue();
            if (alegere != null) {
                if (alegere.equals("Client")) {
                    Parent fxml = FXMLLoader.load(getClass().getResource("/InregistrareClient.fxml"));
                    Scene loginScene = new Scene(fxml);
                    Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    window.setScene(loginScene);
                    window.show();
                } else if (alegere.equals("Centru de adoptie")) {
                    Parent fxml = FXMLLoader.load(getClass().getResource("/InregistrareCentru.fxml"));
                    Scene loginScene = new Scene(fxml);
                    Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    window.setScene(loginScene);
                    window.show();
                } else if (alegere.equals("Petshop")) {
                    Parent fxml = FXMLLoader.load(getClass().getResource("/InregistrarePetshop.fxml"));
                    Scene loginScene = new Scene(fxml);
                    Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    window.setScene(loginScene);
                    window.show();
                }
            } else message("Eroare!","Va rugam alegeti o categorie");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(javafx.event.ActionEvent actionEvent) {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("/Start.fxml"));
            Scene scene = new Scene(fxml);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}