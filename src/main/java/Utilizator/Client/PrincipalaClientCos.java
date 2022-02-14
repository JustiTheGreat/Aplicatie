package Utilizator.Client;

import Classes.AllProducts;
import Classes.Client;
import Classes.Product;
import MyConnection.Utillity;
import MyConnection.MyURL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PrincipalaClientCos extends Utillity {
    private Client client_logat;
    private ArrayList<AllProducts> cart;
    @FXML
    private ListView<String> lv = new ListView<String>();
    @FXML
    private Text cost;

    public void set(Client client_logat, ArrayList<AllProducts> cart) {
        this.client_logat = client_logat;
        this.cart = cart;
        if (this.cart.isEmpty()) {
            lv.getItems().add("Cosul dumneavoastra este gol!");
            cost.setText(Float.toString(price()));
        } else reloadLV();
    }

    private Float price() {
        Float s = Float.valueOf(0);
        for (AllProducts p : cart)
            if (p.getObject().equals("product")) s = s + ((Product) p).getPret();
        return s;
    }

    private void reloadLV() {
        lv.getItems().clear();
        for (AllProducts p : cart)
            lv.getItems().add(p.toLV());
        if (cart.isEmpty()) lv.getItems().add("Cosul dumneavoastra este gol!");
        cost.setText(Float.toString(price()));
    }

    public void proceedOrder() {
        if (cart.isEmpty()) message("Eroare!", "Cosul dumeavoastra e gol!");
        else
            try {
                Long id_comanda = System.currentTimeMillis();
                String tip_comanda="";
                if (cart.get(0).getObject().equalsIgnoreCase("animal"))
                    tip_comanda = "animale";
                else if (cart.get(0).getObject().equalsIgnoreCase("product"))
                    tip_comanda = "produse";
                String parameters = "?id_comanda=" + id_comanda + "&username=" + client_logat.getUsername() + "&judet=" + client_logat.getJudet() + "&localitate="
                        + client_logat.getLocalitate() + "&strada=" + client_logat.getStrada() + "&numar=" + client_logat.getNumar() + "&telefon=" + client_logat.getTelefon()
                        + "&pret=" + price() + "&magazin=" + cart.get(0).getMagazin() + "&tip_comanda=" + tip_comanda;

                URL url = MyURL.getURL("/add-order" + parameters);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.disconnect();

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) ;
                in.close();

                for (AllProducts p : cart) {
                    parameters = "?id_comanda=" + id_comanda + "&id_produs=" + p.getId();
                    url = MyURL.getURL("/add-order-product-bond" + parameters);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.disconnect();

                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((inputLine = in.readLine()) != null) ;
                    in.close();
                }

                message("Mesaj", "Comanda dumneavoastra a\n fost trimisa!");
                cart.clear();
                lv.getItems().clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void deleteProduct() {
        String selectedItem = lv.getSelectionModel().getSelectedItem();
        if (selectedItem == null) message("Eroare!", "Nu ati selectat un produs!");
        else {
            String[] details = selectedItem.split(" ");
            int index = 0;
            for (AllProducts p : cart)
                if (p.getId() == Long.parseLong(details[37]))
                    index = cart.indexOf(p);
            cart.remove(index);
            reloadLV();
        }
    }

    public void principalaClient(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/PrincipalaClient.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            PrincipalaClient controller = loader.getController();
            controller.set(false, 0, client_logat, cart);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void principalaClientMagazine(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/PrincipalaClientMagazine.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            PrincipalaClientMagazine controller = loader.getController();
            controller.set(client_logat, cart);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void principalaClientComenzi(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/PrincipalaClientComenzi.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            PrincipalaClientComenzi controller = loader.getController();
            controller.set(client_logat, cart);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void principalaClientCos(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/PrincipalaClientCos.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            PrincipalaClientCos controller = loader.getController();
            controller.set(client_logat, cart);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deconectare(javafx.event.ActionEvent actionEvent) {
        try {
            File cookie = new File(cookie_file);
            cookie.delete();
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
