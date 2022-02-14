package Utilizator.Magazin.Centru;

import Classes.AllProducts;
import Classes.Animal;
import Classes.Centru;
import MyConnection.Utillity;
import MyConnection.MyURL;
import Utilizator.Magazin.ListaComenzi;
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

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PrincipalaCentru extends Utillity {
    private Centru centru_logat;
    private ArrayList<AllProducts> all_products;
    @FXML
    private ListView<String> lv = new ListView<String>();
    @FXML
    private RadioButton fem;
    @FXML
    private RadioButton masc;
    @FXML
    private RadioButton pisici;
    @FXML
    private RadioButton caini;
    @FXML
    private RadioButton papagali;
    @FXML
    private RadioButton pestisori;
    @FXML
    private RadioButton hamsteri;
    @FXML
    private RadioButton az;
    @FXML
    private RadioButton dn;
    private int az_state = 0, dn_state = 0;

    public void set(Centru centru_logat, ArrayList<AllProducts> all_products) {
        this.centru_logat = centru_logat;
        this.all_products = all_products;
        read();
    }

    public void read() {
        try {
            boolean[] buttons = getButtonsAnimal();
            String parameters = "?magazin=" + centru_logat.getUsername() + "&f1=" + buttons[0] + "&f2=" + buttons[1] + "&f3=" + buttons[2] + "&f4=" + buttons[3]
                    + "&f5=" + buttons[4] + "&f6=" + buttons[5] + "&f7=" + buttons[6] + "&s1=" + buttons[7] + "&s2=" + buttons[8] + "&s3=" + buttons[9] + "&s4=" + buttons[10];
            URL url = MyURL.getURL("/get-animals-magazin" + parameters);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input_line;
            String json_resp = "";
            while ((input_line = in.readLine()) != null)
                json_resp = json_resp + input_line;

            if (json_resp.isEmpty()) {
                message("Database error!", "get-animals-magazin");
                return;
            }
            in.close();
            connection.disconnect();
            Type list_type = new TypeToken<ArrayList<Animal>>() {
            }.getType();
            all_products = new Gson().fromJson(json_resp, list_type);
            loadLV(all_products);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAnimal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PrincipalaCentruAdaugare.fxml"));
            Parent fxml = loader.load();
            Stage stage = new Stage();
            PrincipalaCentruAdaugare controller = loader.getController();
            controller.set(stage, centru_logat, all_products, lv, fem, masc, pisici, caini, papagali, pestisori, hamsteri, az, dn, az_state, dn_state);
            stage.setTitle("Adaugare produs");
            stage.setScene(new Scene(fxml));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editAnimal() {
        String selectedItem = lv.getSelectionModel().getSelectedItem();
        if (selectedItem == null) message("Eroare!", "Nu ati selectat nici un animal!");
        else {
            String[] details = selectedItem.split(" ");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/PrincipalaCentruEditare.fxml"));
                Parent fxml = loader.load();
                Stage stage = new Stage();
                PrincipalaCentruEditare controller = loader.getController();
                controller.set(stage, centru_logat, lv, fem, masc, pisici, caini, papagali, pestisori, hamsteri, az, dn, az_state, dn_state, Long.parseLong(details[37]));
                stage.setTitle("Editare produs");
                stage.setScene(new Scene(fxml));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteAnimal() {
        String selectedItem = lv.getSelectionModel().getSelectedItem();
        if (selectedItem == null) message("Eroare!", "Nu ati selectat un animal!");
        else {
            String[] details = selectedItem.split(" ");
            try {
                String parameters = "?id=" + details[37];
                URL url = MyURL.getURL("/delete-animal" + parameters);
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

    private boolean[] getButtonsAnimal() {
        boolean[] f = {false, false, false, false, false, false, false}, s = {false, false, false, false};
        try {
            if (fem.isSelected()) f[0] = true;
            if (masc.isSelected()) f[1] = true;
            if (pisici.isSelected()) f[2] = true;
            if (caini.isSelected()) f[3] = true;
            if (papagali.isSelected()) f[4] = true;
            if (pestisori.isSelected()) f[5] = true;
            if (hamsteri.isSelected()) f[6] = true;
            if (az.isSelected() && az_state == 0) {
                if (dn.isSelected()) {
                    dn.setSelected(false);
                    dn_state = 0;
                    dn.setText("Pret (crescator)");
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

            if (dn.isSelected() && dn_state == 0) {
                if (az.isSelected()) {
                    s[0] = false;
                    s[1] = false;
                    az.setSelected(false);
                    az_state = 0;
                    az.setText("Alfabetic (A-Z)");
                }
                s[2] = true;
                dn_state = 1;
            } else if (dn.isSelected() && dn_state == 1)
                s[2] = true;
            else if (!dn.isSelected() && dn_state == 1) {
                dn.setText("Data nasterii (descrescator)");
                dn.setSelected(true);
                s[3] = true;
                dn_state = 2;
            } else if (dn.isSelected() && dn_state == 2)
                s[3] = true;
            else if (!dn.isSelected() && dn_state == 2) {
                dn.setText("Data nasterii (crescator)");
                dn_state = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new boolean[]{f[0], f[1], f[2], f[3], f[4], f[5], f[6], s[0], s[1], s[2], s[3]};
    }

    public void viewOrders(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ListaComenzi.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            ListaComenzi controller = loader.getController();
            controller.set(centru_logat);
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
