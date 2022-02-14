package Utilizator.Client;

import Classes.AllProducts;
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

public class PrincipalaClientProduse extends Utillity {
    private Client client_logat;
    private ArrayList<AllProducts> all_products = new ArrayList<AllProducts>();
    private ArrayList<AllProducts> cart;
    @FXML
    private ListView<String> lv = new ListView<String>();
    @FXML
    private RadioButton produse;
    @FXML
    private RadioButton hrana;
    @FXML
    private RadioButton jucarii;
    @FXML
    private RadioButton ingrijire;
    @FXML
    private RadioButton az;
    @FXML
    private RadioButton pret;
    private int az_state = 0, pret_state = 0;

    public void set(boolean az_selected, int az_state, Client client_logat, ArrayList<AllProducts> cart) {
        this.client_logat = client_logat;
        this.cart = cart;
        if (az_selected && az_state == 1) {
            az.setSelected(true);
        } else if (az_selected && az_state == 2) {
            this.az_state = 1;
        }
        produse.setSelected(true);
        read();
    }

    public void read() {
        try {
            boolean[] buttons = getButtonsProduct();
            String parameters = "?f1=" + buttons[0] + "&f2=" + buttons[1] + "&f3=" + buttons[2]
                    + "&s1=" + buttons[3] + "&s2=" + buttons[4] + "&s3=" + buttons[5] + "&s4=" + buttons[6];
            URL url = MyURL.getURL("/get-products" + parameters);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input_line;
            String json_resp = "";
            while ((input_line = in.readLine()) != null)
                json_resp = json_resp + input_line;
            if (json_resp.isEmpty()) {
                message("Database error!", "get-products");
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

    public void principalaClient(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/PrincipalaClient.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            PrincipalaClient controller = loader.getController();
            if (produse.isSelected())
                controller.set(false, 0, client_logat, cart);
            else controller.set(az.isSelected(), az_state, client_logat, cart);
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
}
