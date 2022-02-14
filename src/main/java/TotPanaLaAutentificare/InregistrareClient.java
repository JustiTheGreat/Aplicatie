package TotPanaLaAutentificare;

import MyConnection.Utillity;
import MyConnection.MyURL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InregistrareClient extends Utillity {
    @FXML
    private TextField nume;
    @FXML
    private TextField prenume;
    @FXML
    private TextField judet;
    @FXML
    private TextField localitate;
    @FXML
    private TextField strada;
    @FXML
    private TextField numar;
    @FXML
    private TextField telefon;
    @FXML
    private TextField username;
    @FXML
    private TextField parola;

    public boolean isNumber(String s) {
        try{
            if(Long.parseLong(s)<=0)return false;
        }catch(Exception e)
        {
            return false;
        }
        return true;
    }

    public void inregistrare(javafx.event.ActionEvent actionEvent) {
        if(nume.getText().trim().isEmpty()|| prenume.getText().trim().isEmpty()|| judet.getText().trim().isEmpty()
                || localitate.getText().trim().isEmpty() || strada.getText().trim().isEmpty()|| numar.getText().trim().isEmpty()
                || telefon.getText().trim().isEmpty() || username.getText().trim().isEmpty()|| parola.getText().trim().isEmpty()
        ) message("Eroare!","Completati toate campurile");
        else if(nume.getText().trim().split(" ").length>1 || prenume.getText().trim().split(" ").length>1 || judet.getText().trim().split(" ").length>1
                || localitate.getText().trim().split(" ").length>1 || strada.getText().trim().split(" ").length>1 || numar.getText().trim().split(" ").length>1
                || telefon.getText().trim().split(" ").length>1 || username.getText().trim().split(" ").length>1 || parola.getText().trim().split(" ").length>1
        ) message("Eroare!","Campurile trebuie completate fara spatii");
        else if(!isNumber(telefon.getText().trim()) || telefon.getText().trim().length()!=9 || !isNumber(numar.getText().trim()))
            message("Eroare!","Numarul strazii sau numarul de\ntelefon este intodus gresit");
        else {
            try {
                String parameters = "?username=" + username.getText().trim();
                URL url = MyURL.getURL("/get-center" + parameters);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String input_line;
                String json_resp = "";
                while ((input_line = in.readLine()) != null)
                    json_resp = json_resp + input_line;
                if (json_resp.isEmpty()) {
                    message("Database error!", "get-center");
                    return;
                }
                in.close();
                connection.disconnect();
                if (json_resp.equals("[]")) {
                    url = MyURL.getURL("/get-client" + parameters);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    json_resp = "";
                    while ((input_line = in.readLine()) != null)
                        json_resp = json_resp + input_line;
                    if (json_resp.isEmpty()) {
                        message("Database error!", "get-client");
                        return;
                    }
                    in.close();
                    connection.disconnect();
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
                        if (json_resp.equals("[]")) {
                            parameters = "?username=" + username.getText().trim() + "&parola=" + parola.getText().trim() + "&nume=" + nume.getText().trim()
                                    + "&prenume=" + prenume.getText().trim() + "&judet=" + judet.getText().trim() + "&localitate=" + localitate.getText().trim()
                                    + "&strada=" + strada.getText().trim() + "&numar=" + numar.getText().trim() + "&telefon=" + telefon.getText().trim() + "&categorie=client";
                            url = MyURL.getURL("/add-client" + parameters);
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setDoOutput(true);
                            connection.setRequestMethod("POST");
                            connection.disconnect();

                            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            while ((input_line = in.readLine()) != null) ;
                            in.close();

                            message("Mesaj","V-ati inregistrat cu succes!");
                            start(actionEvent);
                            return;
                        }
                    }
                }
                message("Eroare!","Numele de utilizator exista deja!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void start(javafx.event.ActionEvent actionEvent) {
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

    public void inapoi(javafx.event.ActionEvent actionEvent) {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("/Alegere.fxml"));
            Scene scene = new Scene(fxml);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}