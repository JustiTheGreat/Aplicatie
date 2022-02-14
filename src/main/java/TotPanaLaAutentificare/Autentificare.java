package TotPanaLaAutentificare;

import Classes.*;
import MyConnection.Utillity;
import MyConnection.MyURL;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import Utilizator.Client.PrincipalaClient;
import Utilizator.Magazin.Centru.PrincipalaCentru;
import Utilizator.Magazin.Petshop.PrincipalaPetshop;

public class Autentificare extends Utillity {
    @FXML
    private TextField username;
    @FXML
    private PasswordField parola;

    public void autentificare(javafx.event.ActionEvent actionEvent) {
        try {
            if (username.getText().trim().isEmpty() || parola.getText().trim().isEmpty()) {
                message("Eroare!", "Completati toate campurile");
                return;
            }
            String parameters = "?username=" + username.getText().trim();
            URL url = MyURL.getURL("/get-center" + parameters);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String json_resp = in.readLine();
            if(json_resp==null) {
                message("Database error!","get-center");
                return;
            }
            in.close();
            connection.disconnect();
            Type list_type = new TypeToken<ArrayList<Centru>>() {
            }.getType();
            if (json_resp.equals("[]")) {
                url = MyURL.getURL("/get-client" + parameters);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                json_resp = in.readLine();
                if(json_resp==null) {
                    message("Database error!","get-client");
                    return;
                }
                connection.disconnect();
                list_type = new TypeToken<ArrayList<Client>>() {
                }.getType();
                if (json_resp.equals("[]")) {
                    url = MyURL.getURL("/get-petshop" + parameters);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    json_resp = in.readLine();
                    if(json_resp==null) {
                        message("Database error!","get-petshop");
                        return;
                    }
                    in.close();
                    connection.disconnect();
                    list_type = new TypeToken<ArrayList<Petshop>>() {
                    }.getType();
                    if (json_resp.equals("[]")) {
                        message("Eroare!", "Username sau parola introduse gresit");
                        username.clear();
                        parola.clear();
                        return;
                    }
                }
            }
            Utilizator u = ((ArrayList<Utilizator>) new Gson().fromJson(json_resp, list_type)).get(0);
            if(!parola.getText().trim().equals(u.getParola())) {
                message("Eroare!", "Username sau parola introduse gresit");
                username.clear();
                parola.clear();
                return;
            }
            File cookie = new File(cookie_file);
            cookie.createNewFile();
            FileWriter myWriter = new FileWriter(cookie_file);
            myWriter.write(u.getUsername()+" "+u.getParola()+" "+u.getCategorie());
            myWriter.close();
            logare(u, actionEvent);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void logare(Utilizator u, javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            if (u.getCategorie().equals("client"))
                loader.setLocation(getClass().getResource("/PrincipalaClient.fxml"));
            else if (u.getCategorie().equals("centru"))
                loader.setLocation(getClass().getResource("/PrincipalaCentru.fxml"));
            else if (u.getCategorie().equals("petshop"))
                loader.setLocation(getClass().getResource("/PrincipalaPetshop.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            PrincipalaClient controller1;
            PrincipalaCentru controller2;
            PrincipalaPetshop controller3;
            if (u.getCategorie().equals("client")) {
                controller1 = loader.getController();
                controller1.set(false, 0, (Client) u, new ArrayList<AllProducts>());
            } else if (u.getCategorie().equals("centru")) {
                controller2 = loader.getController();
                controller2.set((Centru) u, new ArrayList<AllProducts>());
            } else if (u.getCategorie().equals("petshop")) {
                controller3 = loader.getController();
                controller3.set((Petshop) u, new ArrayList<AllProducts>());
            }
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void inapoi(javafx.event.ActionEvent actionEvent) {
        try {
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
