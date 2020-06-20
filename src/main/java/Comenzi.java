import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Comenzi {
    private String client_username;
    private String client_adress;
    private ArrayList<AllProducts> cart;
    private ArrayList<Order> comenzi_user=new ArrayList<Order>();
    @FXML
    private ListView<String> lv = new ListView<String>();
    public void set(String client_username,String client_adress,ArrayList<AllProducts> cart) {
        this.client_username = client_username;
        this.client_adress=client_adress;
        this.cart = cart;
        loadLV();
    }
    public void loadLV()
    {
        try {
            lv.getItems().clear();
            Scanner sc = new Scanner(new File("src/main/comenzi.txt"));
            boolean ok = false;
            int index = -1;
            while (sc.hasNextLine())
            {
                String[] details = sc.nextLine().split(" ");
                if (details[1].equals("NUME:" + client_username)) {
                    index++;
                    String[] t = details[0].split(":");
                    String[] u = details[1].split(":");
                    String[] a = details[2].split(":");
                    String[] c = details[3].split(":");
                    String[] s = details[4].split(":");
                    String[] m = details[5].split(":");
                    String[] l = details[6].split(":");
                    comenzi_user.add(new Order(Long.parseLong(t[1]),u[1], a[1], Float.parseFloat(c[1]), s[1], m[1], new ArrayList<AllProducts>(),l[1]));
                    ok = true;
                }
                else if (ok && (details[0].equals("ANIMAL") || details[0].equals("PRODUCT")))
                    if (details[0].equals("ANIMAL"))
                        comenzi_user.get(index).getProduse().add(new Animal(details[1], details[2], details[3], Integer.parseInt(details[4]), details[5], details[6]));
                    else
                        comenzi_user.get(index).getProduse().add(new Product(details[1], Float.parseFloat(details[2]), details[3], details[4], Integer.parseInt(details[5]), details[6]));
                else ok = false;
            }
            if(comenzi_user.isEmpty()) lv.getItems().add("Nu ati trimis nici o comanda!");
            else for (Order o : comenzi_user) {
                String produse = "";
                for (AllProducts p : o.getProduse())
                    if (p.getObject().equals("ANIMAL"))
                        produse = produse + " " + ((Animal) p).getNumeRasa();
                    else produse = produse + " " + ((Product) p).getNume();
                lv.getItems().add("TIME: "+o.getTime()+"   NUME: " + o.getNume() + "   ADRESA: " + o.getAdresa() + "   COST: " + o.getCost() + "   STARE: " + o.getStare() + "   MAGAZIN: " + o.getMagazin() + "   TIMP_LIVRARE: " + o.getTimpLivrare() + "   OBIECTE: " + produse);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void principala(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Principala.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Principala controller = loader.getController();
            controller.set(false, 0,client_username,client_adress, cart);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void magazine(ActionEvent actionEvent) {
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/Magazine.fxml"));
            Parent parent=loader.load();
            Scene scene = new Scene(parent);
            Magazine controller=loader.getController();
            controller.set(client_username,client_adress,cart);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void comenzi(ActionEvent actionEvent) {
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/Comenzi.fxml"));
            Parent parent=loader.load();
            Scene scene = new Scene(parent);
            Comenzi controller=loader.getController();
            controller.set(client_username,client_adress,cart);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void cos(ActionEvent actionEvent) {
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/Cos.fxml"));
            Parent parent=loader.load();
            Scene scene = new Scene(parent);
            Cos controller=loader.getController();
            controller.set(client_username,client_adress,cart);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
