import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Magazine {
    private ArrayList<Utilizatori> List=new ArrayList<Utilizatori>();

    private String client_username;
    private String client_adress;
    private ArrayList<AllProducts> cart;
    @FXML
    private ListView<String> lv = new ListView<String>();

    public void set(String client_username,String client_adress,ArrayList<AllProducts> cart) {
        this.client_username = client_username;
        this.client_adress=client_adress;
        this.cart = cart;
        loadLV();
    }

    public void loadLV(){

        //Decodificare xml file
        try{
            FileInputStream fis = new FileInputStream("./DateUseriXML.xml");
            XMLDecoder decoder = new XMLDecoder(fis);
            ArrayList list = new ArrayList();
            list = (ArrayList) decoder.readObject();
            List =list;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }


        for (int i=0; i<List.size();i++)
            if(List.get(i).getCategorie().equals("Petshop"))
                lv.getItems().add("PETSHOP:     "+((Petshop)List.get(i)).getNumepetshop());
            else if(List.get(i).getCategorie().equals("Centru"))
                lv.getItems().add("CENTRU:      "+((Centru)List.get(i)).getNumecentru());
    }

    public void principala(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Principala.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Principala controller = loader.getController();
            controller.set(false, 0,client_username,client_adress, cart);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void magazine(ActionEvent actionEvent) {
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/Magazine.fxml"));
            Parent parent=loader.load();
            Scene scene = new Scene(parent);
            Magazine controller=loader.getController();
            controller.set(client_username,client_adress,cart);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceseaza(ActionEvent actionEvent) {
        String selectedItem = lv.getSelectionModel().getSelectedItem();
        if (selectedItem == null) message("Error!","Nu ati selectat nici un magazin!");
        else {
            String[] details = selectedItem.split(" ");
            if(details[0].equals("PETSHOP:"))
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/ProdusePetshop.fxml"));
                Parent parent = loader.load();
                Scene scene = new Scene(parent);
                ProdusePetshop controller = loader.getController();
                controller.set(client_username, client_adress, cart, details[5]);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            else if(details[0].equals("CENTRU:"))
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/AnimaleCentru.fxml"));
                    Parent parent = loader.load();
                    Scene scene = new Scene(parent);
                    AnimaleCentru controller = loader.getController();
                    controller.set(client_username, client_adress, cart, details[6]);
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

        public void message(String title,String message) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Message.fxml"));
                Parent fxml = loader.load();
                Stage stage = new Stage();
                Message controller = loader.getController();
                controller.set(message, stage);
                stage.setTitle(title);
                stage.setScene(new Scene(fxml));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
