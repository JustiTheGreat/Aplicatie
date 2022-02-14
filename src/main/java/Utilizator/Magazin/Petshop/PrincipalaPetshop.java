package Utilizator.Magazin.Petshop;

import Classes.AllProducts;
import Classes.Petshop;
import Classes.Product;
import MyConnection.Utillity;
import MyConnection.MyURL;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import Utilizator.Magazin.ListaComenzi;

public class PrincipalaPetshop extends Utillity {
    private Petshop petshop_logat;
    private ArrayList<AllProducts> all_products;
    @FXML
    private ListView<String> lv = new ListView<String>();
    @FXML
    private RadioButton hrana;
    @FXML
    private RadioButton ingrijire;
    @FXML
    private RadioButton jucarii;
    @FXML
    private RadioButton az;
    @FXML
    private RadioButton pret;
    private int az_state = 0, pret_state = 0;

    public void set(Petshop petshop_logat, ArrayList<AllProducts> all_products) {
        this.petshop_logat = petshop_logat;
        this.all_products = all_products;
        read();
    }

    public void read() {
        try {
            boolean[] buttons = getButtonsProduct();
            String parameters = "?magazin=" + petshop_logat.getUsername() + "&f1=" + buttons[0] + "&f2=" + buttons[1] + "&f3=" + buttons[2]
                    + "&s1=" + buttons[3] + "&s2=" + buttons[4] + "&s3=" + buttons[5] + "&s4=" + buttons[6];
            URL url = MyURL.getURL("/get-products-magazin"+parameters);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input_line;
            String json_resp = "";
            while ((input_line = in.readLine()) != null)
                json_resp = json_resp + input_line;
            if (json_resp.isEmpty()) {
                message("Database error!", "get-products-magazin");
                return;
            }
            in.close();
            connection.disconnect();
            Type list_type = new TypeToken<ArrayList<Product>>() {
            }.getType();
            all_products = new Gson().fromJson(json_resp, list_type);
            loadLV(all_products);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PrincipalaPetshopAdaugare.fxml"));
            Parent fxml = loader.load();
            Stage stage = new Stage();
            PrincipalaPetshopAdaugare controller = loader.getController();
            controller.set(stage, petshop_logat, all_products, lv, hrana, ingrijire, jucarii, az, pret, az_state, pret_state);
            stage.setTitle("Adaugare produs");
            stage.setScene(new Scene(fxml));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editProduct() {
        String selectedItem = lv.getSelectionModel().getSelectedItem();
        if (selectedItem == null) message("Eroare!", "Nu ati selectat nici un produs!");
        else {
            String[] details = selectedItem.split(" ");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/PrincipalaPetshopEditare.fxml"));
                Parent fxml = loader.load();
                Stage stage = new Stage();
                PrincipalaPetshopEditare controller = loader.getController();
                controller.set(stage, petshop_logat, lv, hrana, ingrijire, jucarii, az, pret, az_state, pret_state, Long.parseLong(details[37]));
                stage.setTitle("Editare produs");
                stage.setScene(new Scene(fxml));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteProduct() {
        String selectedItem = lv.getSelectionModel().getSelectedItem();
        if (selectedItem == null) message("Eroare!", "Nu ati selectat un produs!");
        else {
            String[] details = selectedItem.split(" ");
            try {
                String parameters = "?id=" + details[37];
                URL url = MyURL.getURL("/delete-product" + parameters);
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

    private void loadLV(ArrayList<AllProducts> all_products) {
        lv.getItems().clear();
        for (AllProducts p : all_products)
            lv.getItems().add(p.toLV());
    }

    private boolean[] getButtonsProduct() {
        boolean[] f = {false, false, false}, s = {false, false, false, false};
        try {
            if (hrana.isSelected()) f[0] = true;
            if (ingrijire.isSelected()) f[1] = true;
            if (jucarii.isSelected()) f[2] = true;
            if (az.isSelected() && az_state == 0) {
                if (pret.isSelected()) {
                    pret.setSelected(false);
                    pret_state = 0;
                    pret.setText("Pret (crescator)");
                }
                s[0] = true;
                az_state = 1;
            } else if (az.isSelected() && az_state == 1)
                s[0] = true;
            else if (!az.isSelected() && az_state == 1) {
                az.setText("Alfabetic (Z-A)");
                az.setSelected(true);
                s[1] = true;
                az_state = 2;
            } else if (az.isSelected() && az_state == 2)
                s[1] = true;
            else if (!az.isSelected() && az_state == 2) {
                az.setText("Alfabetic (A-Z)");
                az_state = 0;
            }

            if (pret.isSelected() && pret_state == 0) {
                if (az.isSelected()) {
                    s[0] = false;
                    s[1] = false;
                    az.setSelected(false);
                    az_state = 0;
                    az.setText("Alfabetic (A-Z)");
                }
                s[2] = true;
                pret_state = 1;
            } else if (pret.isSelected() && pret_state == 1)
                s[2] = true;
            else if (!pret.isSelected() && pret_state == 1) {
                pret.setText("Pret (descrescator)");
                pret.setSelected(true);
                s[3] = true;
                pret_state = 2;
            } else if (pret.isSelected() && pret_state == 2)
                s[3] = true;
            else if (!pret.isSelected() && pret_state == 2) {
                pret.setText("Pret (crescator)");
                pret_state = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new boolean[]{f[0], f[1], f[2], s[0], s[1], s[2], s[3]};
    }

    public void viewOrders(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ListaComenzi.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            ListaComenzi controller = loader.getController();
            controller.set(petshop_logat);
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