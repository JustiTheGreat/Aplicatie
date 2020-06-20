import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class Alegere implements Initializable {
    ObservableList list= FXCollections.observableArrayList();
    @FXML
    private ChoiceBox<String> series;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    private void loadData(){
        list.removeAll(list);
        String a="Client";
        String b="Centru de adoptie";
        String c="Petshop";
        list.addAll(a,b,c);
        series.getItems().addAll(list);
    }

    public void displayValue(javafx.event.ActionEvent actionEvent) throws IOException {
        String alegere=series.getValue();
        if(alegere!=null){
            if(alegere.equals("Client")){
                Parent fxml= FXMLLoader.load(getClass().getResource("/Inregistrare.fxml"));
                Scene loginScene=new Scene(fxml);
                Stage window=(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                window.setScene(loginScene);
                window.show();
            }
            else if(alegere.equals("Centru de adoptie")){
                Parent fxml= FXMLLoader.load(getClass().getResource("/InregistrareCentru.fxml"));
                Scene loginScene=new Scene(fxml);
                Stage window=(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                window.setScene(loginScene);
                window.show();
            }
            else if(alegere.equals("Petshop")){
                Parent fxml= FXMLLoader.load(getClass().getResource("/InregistrarePetshop.fxml"));
                Scene loginScene=new Scene(fxml);
                Stage window=(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                window.setScene(loginScene);
                window.show();
            }
        }
        else eroare("Va rugam alegeti o categorie");
    }
    public void eroare(String s) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Message.fxml"));
            Parent fxml = loader.load();
            Stage stage = new Stage();
            Message controller = loader.getController();
            controller.set(s,stage);
            stage.setTitle("Error!");
            stage.setScene(new Scene(fxml));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}