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

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PrincipalaClientMagazine  extends Utillity {
    private Client client_logat;
    private ArrayList<AllProducts> cart;
    private ArrayList<Utilizator> magazine = new ArrayList<Utilizator>();
    @FXML
    private ListView<String> lv = new ListView<String>();

    public void set(Client client_logat, ArrayList<AllProducts> cart) {
        this.client_logat = client_logat;
        this.cart = cart;
        read();
    }

    public void read() {
        try {
            magazine.clear();
            URL url = MyURL.getURL("/get-centers");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input_line;
            String json_resp = "";
            while ((input_line = in.readLine()) != null)
                json_resp = json_resp + input_line;
            if (json_resp.isEmpty()) {
                message("Database error!", "get-centers");
                return;
            }
            in.close();
            connection.disconnect();
            Type list_type = new TypeToken<ArrayList<Centru>>() {
            }.getType();
            magazine = new Gson().fromJson(json_resp, list_type);

            url = MyURL.getURL("/get-petshops");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            input_line = null;
            json_resp = "";
            while ((input_line = in.readLine()) != null)
                json_resp = json_resp + input_line;
            if (json_resp.isEmpty()) {
                message("Database error!", "get-petshops");
                return;
            }
            in.close();
            connection.disconnect();
            list_type = new TypeToken<ArrayList<Petshop>>() {
            }.getType();
            magazine.addAll(((ArrayList<Utilizator>) new Gson().fromJson(json_resp, list_type)));
            loadLV(magazine);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLV(ArrayList<Utilizator> magazine) {
        lv.getItems().clear();
        for (Utilizator u : magazine)
            lv.getItems().add(u.toString());
    }

    public void acceseaza(ActionEvent actionEvent) {
        String selectedItem = lv.getSelectionModel().getSelectedItem();
        if (selectedItem == null) message("Eroare!", "Nu ati selectat nici un magazin!");
        else try {
            String[] details = selectedItem.split(" ");
            String parameters = "?username=" + details[1];
            if (details[3].equals("centru")) {
                URL url = MyURL.getURL("/get-center" + parameters);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String json_resp = in.readLine();
                if (json_resp == null) {
                    message("Database error!", "get-centru");
                    return;
                }
                in.close();
                connection.disconnect();
                Type list_type = new TypeToken<ArrayList<Centru>>() {
                }.getType();
                Centru magazin = ((ArrayList<Centru>) new Gson().fromJson(json_resp, list_type)).get(0);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/PrincipalaClientMagazineCentru.fxml"));
                Parent parent = loader.load();
                Scene scene = new Scene(parent);
                PrincipalaClientMagazineCentru controller = loader.getController();
                controller.set(client_logat, cart, magazin);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else if (details[3].equals("petshop")) {
                URL url = MyURL.getURL("/get-petshop" + parameters);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String json_resp = in.readLine();
                if (json_resp == null) {
                    message("Database error!", "get-petshop");
                    return;
                }
                in.close();
                connection.disconnect();
                Type list_type = new TypeToken<ArrayList<Petshop>>() {
                }.getType();
                Petshop magazin = ((ArrayList<Petshop>) new Gson().fromJson(json_resp, list_type)).get(0);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/PrincipalaClientMagazinePetshop.fxml"));
                Parent parent = loader.load();
                Scene scene = new Scene(parent);
                PrincipalaClientMagazinePetshop controller = loader.getController();
                controller.set(client_logat, cart, magazin);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
