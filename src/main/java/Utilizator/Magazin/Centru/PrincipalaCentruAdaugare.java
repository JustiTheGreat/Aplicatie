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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PrincipalaCentruAdaugare extends Utillity {
    private Centru centru_logat;
    private ArrayList<AllProducts> all_products;
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
    private TextField denumire;
    @FXML
    private TextField anul_nasterii;
    @FXML
    private ChoiceBox categorie;
    @FXML
    private ChoiceBox sex;

    public void set(Stage stage, Centru centru_logat, ArrayList<AllProducts> all_products, ListView<String> lv, RadioButton fem, RadioButton masc, RadioButton pisici, RadioButton caini, RadioButton papagali, RadioButton pestisori, RadioButton hamsteri, RadioButton az, RadioButton dn, int az_state, int dn_state) {
        this.stage = stage;
        this.centru_logat = centru_logat;
        this.all_products = all_products;
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
        sex.getItems().addAll("feminin", "masculin");
        categorie.getItems().addAll("pisici", "caini", "papagali", "pestisori", "hamsteri");
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

    public void addLV() {
        try {
            if (denumire.getText().isEmpty() || anul_nasterii.getText().isEmpty() || sex == null || categorie == null)
                message("Eroare!", "Nu ati completat toate campurile");
            else if (denumire.getText().trim().split(" ").length > 1 || anul_nasterii.getText().trim().split(" ").length > 1)
                message("Eroare!", "Campurile trebuie completate fara spatii");
            else if (!isInt(anul_nasterii.getText().trim()) || Integer.parseInt(anul_nasterii.getText()) < 0)
                message("Eroare!", "Anul nasterii introdus incorect");
            else {
                try {
                    Long id_animal = System.currentTimeMillis();
                    String parameters = "?denumire=" + denumire.getText().trim() + "&categorie=" + categorie.getValue().toString().trim() + "&sex="
                            + sex.getValue().toString().trim() + "&anul_nasterii=" + anul_nasterii.getText().trim() + "&magazin=" + centru_logat.getUsername() + "&id=" + id_animal;
                    URL url = MyURL.getURL("/add-animal" + parameters);
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
        } catch (Exception e) {
            message("Eroare!", "Categorie neselectata!");
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
            all_products = new Gson().fromJson(json_resp, list_type);
            loadLV(all_products);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        stage.close();
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

    private boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
