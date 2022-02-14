import Classes.*;

import MyConnection.MyURL;
import MyConnection.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import Utilizator.Client.PrincipalaClient;
import Utilizator.Magazin.Centru.PrincipalaCentru;
import Utilizator.Magazin.Petshop.PrincipalaPetshop;

import static MyConnection.Utillity.cookie_file;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        cookie(primaryStage);
    }

    public void cookie(Stage primaryStage) {
        try {
            File cookie = new File(cookie_file);
            if (cookie.createNewFile()) {
                cookie.delete();
                Parent fxml= FXMLLoader.load(getClass().getResource("/Start.fxml"));
                Scene startScene=new Scene(fxml);
                primaryStage.setScene(startScene);
                primaryStage.setTitle("Aplicatie: Classes.Petshop - Classes.Centru de adoptie animale");
                primaryStage.show();
            } else {
                Scanner myReader = new Scanner(cookie);
                String[] data = myReader.nextLine().split(" ");
                myReader.close();
                String parameters = "?username=" + data[0];
                if(data[2].equals("centru")) {
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
                    Gson gson = new Gson();
                    Type list_type = new TypeToken<ArrayList<Centru>>() {
                    }.getType();
                    ArrayList<Utilizator> u = gson.fromJson(json_resp, list_type);
                    if(u!=null) logare(u.get(0),primaryStage);

                }
                else if(data[2].equals("client")) {
                    URL url = MyURL.getURL("/get-client" + parameters);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String json_resp = in.readLine();
                    if(json_resp==null) {
                        message("Database error!","get-client");
                        return;
                    }
                    in.close();
                    connection.disconnect();
                    Gson gson = new Gson();
                    Type list_type = new TypeToken<ArrayList<Client>>() {
                    }.getType();
                    ArrayList<Utilizator> u = gson.fromJson(json_resp, list_type);;
                    if(u!=null) logare(u.get(0),primaryStage);
                }
                else if(data[2].equals("petshop")) {
                    URL url = MyURL.getURL("/get-petshop" + parameters);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String json_resp = in.readLine();
                    if(json_resp==null) {
                        message("Database error!","get-petshop");
                        return;
                    }
                    in.close();
                    connection.disconnect();
                    Gson gson = new Gson();
                    Type list_type = new TypeToken<ArrayList<Petshop>>() {
                    }.getType();
                    ArrayList<Utilizator> u = gson.fromJson(json_resp, list_type);;
                    if(u!=null) logare(u.get(0),primaryStage);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void logare(Utilizator u, Stage primaryStage) {
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
                controller1.set(false, 0,(Client) u, new ArrayList<AllProducts>());
            } else if (u.getCategorie().equals("centru")) {
                controller2 = loader.getController();
                controller2.set((Centru) u, new ArrayList<AllProducts>());
            } else if (u.getCategorie().equals("petshop")) {
                controller3 = loader.getController();
                controller3.set((Petshop) u, new ArrayList<AllProducts>());
            }
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void message(String title,String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Message.fxml"));
            Parent fxml = loader.load();
            Stage stage = new Stage();
            Message controller = loader.getController();
            controller.set(message, stage);
            stage.setTitle(title);
            stage.setScene(new Scene(fxml));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
