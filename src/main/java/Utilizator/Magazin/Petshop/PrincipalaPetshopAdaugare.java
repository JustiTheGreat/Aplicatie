package Utilizator.Magazin.Petshop;

import Classes.AllProducts;
import Classes.Petshop;
import Classes.Product;
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

public class PrincipalaPetshopAdaugare extends Utillity {
    private Petshop petshop_logat;
    private ArrayList<AllProducts> all_products;
    private ListView<String> lv;
    private RadioButton hrana;
    private RadioButton ingrijire;
    private RadioButton jucarii;
    private RadioButton az;
    private RadioButton price;
    private int az_state,price_state;
    private Stage stage;
    @FXML
    private TextField denumire;
    @FXML
    private TextField pret;
    @FXML
    private ChoiceBox categorie;
    @FXML
    private TextField cantitate;

    public void set(Stage stage, Petshop petshop_logat,ArrayList<AllProducts> all_products, ListView<String> lv, RadioButton hrana, RadioButton ingrijire, RadioButton jucarii, RadioButton az, RadioButton price,int az_state,int price_state) {
        this.stage = stage;
        this.petshop_logat=petshop_logat;
        this.all_products = all_products;
        this.lv = lv;
        this.hrana = hrana;
        this.ingrijire = ingrijire;
        this.jucarii = jucarii;
        this.az=az;
        this.price=price;
        categorie.getItems().addAll("hrana","ingrijire","jucarii");
        if (az.isSelected())
        {
            this.price.setSelected(false);
            this.price_state=0;
            if (az_state == 1) {
                this.az.setSelected(true);
                this.az_state = 0;
            } else if (az_state == 2) {
                this.az.setSelected(false);
                this.az_state = 1;
            }
        }
        else if (price.isSelected())
        {
            this.az.setSelected(false);
            this.az_state=0;
            if (price_state == 1) {
                this.price.setSelected(true);
                this.price_state = 0;
            } else if (price_state == 2) {
                this.price.setSelected(false);
                this.price_state = 1;
            }
        }
        else
        {
            this.az.setSelected(false);
            this.az_state=0;
            this.price.setSelected(false);
            this.price_state=0;
        }
    }

    public void addLV () {
        try {
            if (denumire.getText().isEmpty() || pret.getText().isEmpty() || cantitate.getText().isEmpty() || categorie == null)
                message("Eroare!", "Nu ati completat toate campurile");
            else if(denumire.getText().trim().split(" ").length>1 || pret.getText().trim().split(" ").length>1 || cantitate.getText().trim().split(" ").length>1)
                message ("Eroare!","Campurile trebuie completate fara spatii");
            else if(!isFloat(pret.getText().trim()) || Float.parseFloat(pret.getText().trim()) <= 0 || !isInt(cantitate.getText().trim()) || Integer.parseInt(cantitate.getText()) <= 0)
                message ("Eroare!","Pret sau cantitate introduse incorect");
            else {
                try {
                    Long id_produs = System.currentTimeMillis();
                    String parameters = "?denumire="+denumire.getText().trim()+"&categorie="+categorie.getValue().toString().trim()+"&pret="
                            +pret.getText().trim() +"&cantitate="+cantitate.getText().trim()+"&magazin="+petshop_logat.getUsername() +"&id=" +id_produs;
                    URL url = MyURL.getURL("/add-product" + parameters);
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
        } catch(Exception e) {
            message("Eroare!", "Categorie neselectata!");
            e.printStackTrace();
        }
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

    public void close() {
        stage.close();
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
                if (price.isSelected()) {
                    price.setSelected(false);
                    price_state = 0;
                    price.setText("Pret (crescator)");
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

            if (price.isSelected() && price_state == 0) {
                if (az.isSelected()) {
                    s[0] = false;
                    s[1] = false;
                    az.setSelected(false);
                    az_state = 0;
                    az.setText("Alfabetic (A-Z)");
                }
                s[2] = true;
                price_state = 1;
            } else if (price.isSelected() && price_state == 1)
                s[2] = true;
            else if (!price.isSelected() && price_state == 1) {
                price.setText("Pret (descrescator)");
                price.setSelected(true);
                s[3] = true;
                price_state = 2;
            } else if (price.isSelected() && price_state == 2)
                s[3] = true;
            else if (!price.isSelected() && price_state == 2) {
                price.setText("Pret (crescator)");
                price_state = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new boolean[]{f[0], f[1], f[2], s[0], s[1], s[2], s[3]};
    }

    private boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isFloat(String s) {
        try{
            Float.parseFloat(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}