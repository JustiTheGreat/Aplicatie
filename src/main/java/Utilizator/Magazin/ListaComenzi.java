package Utilizator.Magazin;

import Classes.*;
import MyConnection.MyURL;
import MyConnection.Utillity;
import Utilizator.Magazin.Centru.PrincipalaCentru;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import Utilizator.Magazin.Petshop.PrincipalaPetshop;

public class ListaComenzi extends Utillity {
    private Utilizator magazin_logat;
    private ArrayList<AllProducts> all_products;
    @FXML
    private ListView<String> lv = new ListView<String>();

    public void set(Utilizator magazin_logat) {
        this.magazin_logat = magazin_logat;
        this.all_products = all_products;
        read();
    }

    public void read() {
        try {
            String parameters = "?username=" + magazin_logat.getUsername();
            URL url = MyURL.getURL("/get-orders-magazin" + parameters);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input_line;
            String json_resp = "";
            while ((input_line = in.readLine()) != null)
                json_resp = json_resp + input_line;
            if (json_resp.isEmpty()) {
                message("Database error!", "get-orders-magazin");
                return;
            }
            in.close();
            connection.disconnect();
            Type list_type = new TypeToken<ArrayList<Order>>() {
            }.getType();
            ArrayList<Order> orders = new Gson().fromJson(json_resp, list_type);

            for (Order o : orders) {
                parameters = "?id_comanda=" + o.getId();
                url = MyURL.getURL("/get-order-products" + parameters);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                json_resp = "";
                while ((input_line = in.readLine()) != null)
                    json_resp = json_resp + input_line;
                if (json_resp.isEmpty()) {
                    message("Database error!", "get-order-products");
                    return;
                }
                in.close();
                connection.disconnect();
                if (o.getTip_comanda().equalsIgnoreCase("animale"))
                    list_type = new TypeToken<ArrayList<Animal>>() {
                    }.getType();
                else if (o.getTip_comanda().equalsIgnoreCase("produse"))
                    list_type = new TypeToken<ArrayList<Product>>() {
                    }.getType();
                ArrayList<AllProducts> ap = new Gson().fromJson(json_resp, list_type);
                o.setProduse(ap);
            }

            lv.getItems().clear();
            if (orders.isEmpty()) lv.getItems().add("Nu ati primit nici o comanda!");
            else for (Order o : orders)
                lv.getItems().add(comandaMagazin(o));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String comandaMagazin(Order o) {
        String s = o.toStringMagazin();
        String prod = "";
        for (AllProducts p : o.getProduse())
            if (prod.equals(""))
                prod = prod + p.getDenumire();
            else
                prod = prod + ", " + p.getDenumire();
        return s + prod;
    }

    public void editOrder() {
        String selectedItem = lv.getSelectionModel().getSelectedItem();
        if (selectedItem == null) message("Eroare!", "Nu ati selectat nici o comanda!");
        else {
            String[] details = selectedItem.split(" ");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListaComenziEditare.fxml"));
                Parent fxml = loader.load();
                Stage stage = new Stage();
                ListaComenziEditare controller = loader.getController();
                controller.set(stage, magazin_logat, lv, Long.parseLong(details[1]));
                stage.setTitle("Editare comanda");
                stage.setScene(new Scene(fxml));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteOrder() {
        String selectedItem = lv.getSelectionModel().getSelectedItem();
        if (selectedItem == null) message("Eroare!", "Nu ati selectat o comanda!");
        else {
            String[] details = selectedItem.split(" ");
            try {
                String parameters = "?id=" + details[1];
                URL url = MyURL.getURL("/delete-order" + parameters);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.disconnect();

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) ;
                in.close();

                read();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listaProduse(javafx.event.ActionEvent actionEvent) {
        try {
            if (magazin_logat.getCategorie().equalsIgnoreCase("petshop")) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/PrincipalaPetshop.fxml"));
                Parent parent = loader.load();
                Scene scene = new Scene(parent);
                PrincipalaPetshop controller = loader.getController();
                controller.set((Petshop)magazin_logat, all_products);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
            else if (magazin_logat.getCategorie().equalsIgnoreCase("centru")){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/PrincipalaCentru.fxml"));
                Parent parent = loader.load();
                Scene scene = new Scene(parent);
                PrincipalaCentru controller = loader.getController();
                controller.set((Centru)magazin_logat, all_products);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
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