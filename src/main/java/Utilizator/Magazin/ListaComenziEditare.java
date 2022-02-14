package Utilizator.Magazin;

import Classes.*;
import MyConnection.MyURL;
import MyConnection.Utillity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;

public class ListaComenziEditare extends Utillity {
    private Utilizator magazin_logat;
    @FXML
    private ListView<String> lv = new ListView<String>();
    private Stage stage;
    private Order order;
    @FXML
    private ChoiceBox stare;
    private Long id;

    public void set(Stage stage, Utilizator magazin_logat, ListView<String> lv, Long id) {
        this.stage = stage;
        this.magazin_logat = magazin_logat;
        this.lv=lv;
        this.id=id;
        order = getOrder();
        setCaracteristici();
    }

    private Order getOrder() {
        Order order = null;
        try {
            String parameters = "?id=" + id;
            URL url = MyURL.getURL("/get-order" + parameters);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");connection.disconnect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input_line;
            String json_resp = "";
            while ((input_line = in.readLine()) != null)
                json_resp = json_resp + input_line;
            if (json_resp.isEmpty()||json_resp.equals("[]")) {
                message("Database error!", "get-order");
                return order;
            }
            in.close();
            //connection.disconnect();
            Type list_type = new TypeToken<ArrayList<Order>>() {
            }.getType();
            order = ((ArrayList<Order>) new Gson().fromJson(json_resp, list_type)).get(0);

            parameters = "?id_comanda=" + order.getId();
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
                return order;
            }
            in.close();
            connection.disconnect();
            if (order.getTip_comanda().equalsIgnoreCase("animale"))
                list_type = new TypeToken<ArrayList<Animal>>() {
                }.getType();
            else if (order.getTip_comanda().equalsIgnoreCase("produse"))
                list_type = new TypeToken<ArrayList<Product>>() {
                }.getType();
            order.setProduse((ArrayList<AllProducts>) new Gson().fromJson(json_resp, list_type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    private void setCaracteristici() {
        stare.getItems().addAll("primita","expediata","anulata");
        stare.setValue(order.getStare());
    }

    public void close() { stage.close(); }

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
                if (o.getTip_comanda().equalsIgnoreCase("ANIMALE"))
                    list_type = new TypeToken<ArrayList<Animal>>() {
                    }.getType();
                else if (o.getTip_comanda().equalsIgnoreCase("PRODUSE"))
                    list_type = new TypeToken<ArrayList<Product>>() {
                    }.getType();
                o.setProduse(new Gson().fromJson(json_resp, list_type));
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

    public void editLV () {
        if(stare.getValue().toString().equals("primita")) {
            if(order.getStare().equals("expediata"))message("Eroare!","Comanda a fost deja expediata!");
            else if(order.getStare().equals("anulata"))message("Eroare!","Comanda a fost deja anulata!");
        }
        else if(stare.getValue().toString().equals("expediata"))
        {
            if(order.getStare().equals("trimisa")) {
                boolean realizabil = true;
                ArrayList<Integer> cant = new ArrayList<Integer>();

                if (magazin_logat.getCategorie().equalsIgnoreCase("petshop"))
                    for (AllProducts p : order.getProduse())
                        cant.add(((Product) p).getCantitate());

                for (AllProducts p : order.getProduse())
                    if (magazin_logat.getCategorie().equalsIgnoreCase("centru")) {
                        if (p.getDisponibilitate().equals("indisponibil"))
                            realizabil = false;
                    } else if (magazin_logat.getCategorie().equalsIgnoreCase("petshop"))
                        cant.set(order.getProduse().indexOf(p), ((Product) p).getCantitate() - 1);

                for (Integer i : cant) if (i < 0) realizabil = false;

                if (!realizabil) message("Eroare!", "Comanda nu poate fi realizata!");
                else {
                    try {
                        for (AllProducts p : order.getProduse())
                            if (magazin_logat.getCategorie().equalsIgnoreCase("centru")) {
                                String parameters = "?id=" + p.getId() + "&disponibilitate=" + "indisponibil";
                                URL url = MyURL.getURL("/edit-animal" + parameters);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setDoOutput(true);
                                connection.setRequestMethod("POST");
                                connection.disconnect();
                                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                String inputLine;
                                while ((inputLine = in.readLine()) != null) ;
                                in.close();
                            } else if (magazin_logat.getCategorie().equalsIgnoreCase("petshop")) {
                                String parameters = "?id=" + p.getId() + "&denumire=" + p.getDenumire() + "&categorie=" + p.getCategorie() + "&pret="
                                        + ((Product)p).getPret() + "&cantitate=" + (((Product)p).getCantitate()-1);
                                URL url = MyURL.getURL("/edit-product" + parameters);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setDoOutput(true);
                                connection.setRequestMethod("POST");
                                connection.disconnect();

                                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                String inputLine;
                                while ((inputLine = in.readLine()) != null) ;
                                in.close();
                            }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    order.setStare("expediata");
                    order.setTimp_livrare(order.getJudet_client().equalsIgnoreCase(magazin_logat.getJudet()) ? "24h" : "48h");
                    editOrder();
                    read();
                }
            }
            else if(order.getStare().equals("expediata"))message("Eroare!","Comanda a fost deja expediata!");
            else if(order.getStare().equals("anulata"))message("Eroare!","Comanda a fost deja anulata!");
        }
        else if(stare.getValue().equals("anulata")) {
            if(order.getStare().equals("trimisa")) order.setStare("anulata");
            else if(order.getStare().equals("expediata")) message("Eroare!","Comanda a fost deja expediata!");
            else if(order.getStare().equals("anulata")) message("Eroare!","Comanda a fost deja anulata!");
            editOrder();
            read();
        }
        stage.close();
    }

    public void editOrder() {
        try {
            String parameters = "?id=" + order.getId() + "&stare=" + order.getStare() + "&timp_livrare=" + order.getTimp_livrare();
            URL url = MyURL.getURL("/edit-order" + parameters);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.disconnect();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) ;
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}