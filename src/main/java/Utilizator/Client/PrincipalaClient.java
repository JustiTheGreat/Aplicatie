package Utilizator.Client;

import Classes.AllProducts;
import Classes.Animal;
import Classes.Client;
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

public class PrincipalaClient extends Utillity {
    private Client client_logat;
    private ArrayList<AllProducts> all_products = new ArrayList<AllProducts>();
    private ArrayList<AllProducts> cart;
    @FXML
    private ListView<String> lv = new ListView<String>();
    @FXML
    private RadioButton az;
    private int az_state = 0;

    public void set(boolean az_selected, int az_state, Client client_logat, ArrayList<AllProducts> cart) {
        this.client_logat = client_logat;
        this.cart = cart;
        if (az_selected && az_state == 1) {
            az.setSelected(true);
        } else if (az_selected && az_state == 2) {
            this.az_state = 1;
        }
        read();
    }

    public void read() {
        all_products.clear();
        try {
            boolean[] buttons = new boolean[]{false, false, false, false, false, false, false, false, false, false, false};
            String parameters = "?f1=" + buttons[0] + "&f2=" + buttons[1] + "&f3=" + buttons[2] + "&f4=" + buttons[3] + "&f5=" + buttons[4] + "&f6=" + buttons[5] + "&f7=" + buttons[6]
                    + "&s1=" + buttons[7] + "&s2=" + buttons[8] + "&s3=" + buttons[9] + "&s4=" + buttons[10];
            URL url = MyURL.getURL("/get-animals" + parameters);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input_line;
            String json_resp = "";
            while ((input_line = in.readLine()) != null)
                json_resp = json_resp + input_line;
            if (json_resp.isEmpty()) {
                message("Database error!", "get-animals");
                return;
            }
            in.close();
            connection.disconnect();
            Type list_type = new TypeToken<ArrayList<Animal>>() {
            }.getType();
            all_products = new Gson().fromJson(json_resp, list_type);

            buttons = new boolean[]{false, false, false, false, false, false, false};
            parameters = "?f1=" + buttons[0] + "&f2=" + buttons[1] + "&f3=" + buttons[2]
                    + "&s1=" + buttons[3] + "&s2=" + buttons[4] + "&s3=" + buttons[5] + "&s4=" + buttons[6];
            url = MyURL.getURL("/get-products" + parameters);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            input_line = null;
            json_resp = "";
            while ((input_line = in.readLine()) != null)
                json_resp = json_resp + input_line;
            if (json_resp.isEmpty()) {
                message("Database error!", "get-products");
                return;
            }
            in.close();
            connection.disconnect();
            list_type = new TypeToken<ArrayList<Product>>() {
            }.getType();
            all_products.addAll(((ArrayList<AllProducts>) new Gson().fromJson(json_resp, list_type)));

            sort(all_products);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addToCart() {
        String selectedItem = lv.getSelectionModel().getSelectedItem();
        if (selectedItem == null) message("Eroare!", "Nu ati selectat un produs!");
        else {
            String[] details = selectedItem.split(" ");
            int index = 0;
            for (AllProducts p : all_products)
                if (p.getId() == Long.parseLong(details[37]))
                    index = all_products.indexOf(p);
            if (all_products.get(index).getDisponibilitate().equals("indisponibil"))
                message("Eroare!", "Produsul e indisponibil momentan!");
            else if (cart.isEmpty()) {
                cart.add(all_products.get(index));
                message("Mesaj", "Produsul a fost adaugat in cos");
            } else {
                AllProducts p0 = cart.get(0);
                AllProducts pn = all_products.get(index);
                if (p0.getObject().equals("animal") && pn.getObject().equals("animal") && p0.getMagazin().equals(pn.getMagazin()))
                    if (cart.contains(pn))
                        message("Eroare!", "Animalul este deja adaugat!");
                    else {
                        cart.add(pn);
                        message("Mesaj!", "Produsul a fost adaugat in cos");
                    }
                else if ((p0.getObject().equals("product")) && pn.getObject().equals("product") && p0.getMagazin().equals(pn.getMagazin())) {
                    int nr = 1;
                    for (AllProducts p : cart)
                        if (p.getId() == pn.getId())
                            nr++;
                    if (nr > ((Product) pn).getCantitate())
                        message("Eroare!", "Stocul este insuficient pentru cerere!");
                    else {
                        cart.add(pn);
                        message("Mesaj!", "Produsul a fost adaugat in cos");
                    }
                } else message("Eroare!", "Produsule din cos nu sunt\nde la acelasi magazin!");
            }
        }
    }

    public void principalaClientAnimale(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/PrincipalaClientAnimale.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            PrincipalaClientAnimale controller = loader.getController();
            controller.set(az.isSelected(), az_state, client_logat, cart);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void principalaClientProduse(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/PrincipalaClientProduse.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            PrincipalaClientProduse controller = loader.getController();
            controller.set(az.isSelected(), az_state, client_logat, cart);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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

    private void sort(ArrayList<AllProducts> all_products) {
        boolean[] s = {false, false};
        try {
            if (az.isSelected() && az_state == 0) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        lv.getItems().clear();
        ArrayList<AllProducts> init = new ArrayList<AllProducts>(all_products);
        ArrayList<AllProducts> sort = new ArrayList<AllProducts>();
        if (!init.isEmpty()) {
            if (s[0] || s[1]) {
                int i, n = init.size();
                for (i = 0; i < n; i++) {
                    AllProducts aux = init.get(0);
                    for (AllProducts p : init)
                        if (s[0] && p.getDenumire().toUpperCase().compareTo(aux.getDenumire().toUpperCase()) < 0
                                || s[1] && p.getDenumire().toUpperCase().compareTo(aux.getDenumire().toUpperCase()) > 0)
                            aux = p;
                    sort.add(aux);
                    init.remove(aux);
                }
            } else sort.addAll(init);
        }
        loadLV(sort);
    }

    private void loadLV(ArrayList<AllProducts> all_products) {
        lv.getItems().clear();
        for (AllProducts p : all_products)
            lv.getItems().add(p.toLV());
    }
}

