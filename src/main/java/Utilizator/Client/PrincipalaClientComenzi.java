package Utilizator.Client;

import Classes.*;
import MyConnection.Utillity;
import MyConnection.MyURL;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
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
import java.util.ArrayList;

public class PrincipalaClientComenzi extends Utillity {
    private Client client_logat;
    private ArrayList<AllProducts> cart;
    @FXML
    private ListView<String> lv = new ListView<String>();

    public void set(Client client_logat, ArrayList<AllProducts> cart) {
        this.client_logat = client_logat;
        this.cart = cart;
        read();
    }

    public void read() {
        try {
            String parameters = "?username=" + client_logat.getUsername();
            URL url = MyURL.getURL("/get-orders-client" + parameters);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input_line;
            String json_resp = "";
            while ((input_line = in.readLine()) != null)
                json_resp = json_resp + input_line;
            if (json_resp.isEmpty()) {
                message("Database error!", "get-orders-client");
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
                o.setProduse(new Gson().fromJson(json_resp, list_type));
            }

            lv.getItems().clear();
            if (orders.isEmpty()) lv.getItems().add("Nu ati trimis nici o comanda!");
            else for (Order o : orders)
                lv.getItems().add(comandaClient(o));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String comandaClient(Order o) {
        String s = o.toStringClient();
        String prod = "";
        for (AllProducts p : o.getProduse())
            if (prod.equals(""))
                prod = prod + p.getDenumire();
            else
                prod = prod + ", " + p.getDenumire();
        return s + prod;
    }

    public void principalaClient(ActionEvent actionEvent) {
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

    public void principalaClientMagazine(ActionEvent actionEvent) {
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

    public void principalaClientComenzi(ActionEvent actionEvent) {
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

    public void principalaClientCos(ActionEvent actionEvent) {
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

    public void deconectare(ActionEvent actionEvent) {
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
