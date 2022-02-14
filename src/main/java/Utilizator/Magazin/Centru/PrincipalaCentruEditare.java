package Utilizator.Magazin.Centru;

import Classes.AllProducts;
import Classes.Animal;
import Classes.Centru;
import MyConnection.Utillity;
import MyConnection.MyURL;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PrincipalaCentruEditare extends Utillity {
    private Centru centru_logat;
    private ListView<String> lv;
    private RadioButton fem;
    private RadioButton masc;
    private RadioButton pisici;
    private RadioButton caini;
    private RadioButton papagali;
    private RadioButton pestisori;
    private RadioButton hamsteri;
    private RadioButton az;
    private RadioButton dn;
    private int az_state, dn_state;
    private Stage stage;
    @FXML
    private ChoiceBox disp;
    private Long id;

    public void set(Stage stage, Centru centru_logat, ListView<String> lv, RadioButton fem, RadioButton masc, RadioButton pisici, RadioButton caini, RadioButton papagali, RadioButton pestisori, RadioButton hamsteri, RadioButton az, RadioButton dn, int az_state, int dn_state, Long id) {
        this.stage = stage;
        this.centru_logat = centru_logat;
        this.lv = lv;
        this.fem = fem;
        this.masc = masc;
        this.pisici = pisici;
        this.caini = caini;
        this.papagali = papagali;
        this.pestisori = pestisori;
        this.hamsteri = hamsteri;
        this.az = az;
        this.dn = dn;
        this.id=id;
        disp.getItems().addAll("disponibil", "indisponibil");
        disp.setValue(getAnimal().getDisponibilitate());
        if (az.isSelected()) {
            this.dn.setSelected(false);
            this.dn_state = 0;
            if (az_state == 1) {
                this.az.setSelected(true);
                this.az_state = 0;
            } else if (az_state == 2) {
                this.az.setSelected(false);
                this.az_state = 1;
            }
        } else if (dn.isSelected()) {
            this.az.setSelected(false);
            this.az_state = 0;
            if (dn_state == 1) {
                this.dn.setSelected(true);
                this.dn_state = 0;
            } else if (dn_state == 2) {
                this.dn.setSelected(false);
                this.dn_state = 1;
            }
        } else {
            this.az.setSelected(false);
            this.az_state = 0;
            this.dn.setSelected(false);
            this.dn_state = 0;
        }
    }

    public Animal getAnimal() {
        Animal a = null;
        try {
            String parameters = "?id=" + id;
            URL url = MyURL.getURL("/get-animal" + parameters);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input_line;
            String json_resp = "";
            while ((input_line = in.readLine()) != null)
                json_resp = json_resp + input_line;
            if (json_resp.isEmpty()) {
                message("Database error!", "get-animal");
                return a;
            }
            in.close();
            connection.disconnect();
            Type list_type = new TypeToken<ArrayList<Animal>>() {
            }.getType();
            a = ((ArrayList<Animal>)new Gson().fromJson(json_resp, list_type)).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return a;
    }

    public void editLV() {
        try {
            String parameters = "?id=" + id + "&disponibilitate=" + disp.getValue().toString();
            URL url = MyURL.getURL("/edit-animal" + parameters);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.disconnect();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) ;
            in.close();

            read();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            loadLV((ArrayList<AllProducts>) new Gson().fromJson(json_resp, list_type));
        } catch (Exception e) {
            e.printStackTrace();
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

    public void close() {
        stage.close();
    }
}