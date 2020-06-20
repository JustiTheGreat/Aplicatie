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

public class ListaComenzi {
    private String shop_username;
    private String shop_adress;
    private ArrayList<AllProducts> all_products;
    private ArrayList<Order> shop_orders=new ArrayList<Order>();
    @FXML
    private ListView<String> lv = new ListView<String>();
    private String tip;
    public void set(String shop_username,String shop_adress,ArrayList<AllProducts> all_products,String tip) {
        this.shop_username=shop_username;
        this.shop_adress=shop_adress;
        this.all_products=all_products;
        this.tip=tip;
        loadLV();
    }
    public void loadLV()
    {
        try {
            lv.getItems().clear();
            Scanner sc = new Scanner(new File("src/main/comenzi.txt"));
            int index = -1;
            while (sc.hasNextLine())
            {
                String[] details = sc.nextLine().split(" ");
                if (details[0].equals("ANIMAL") || details[0].equals("PRODUCT"))
                    if (details[0].equals("ANIMAL"))
                        shop_orders.get(index).getProduse().add(new Animal(details[1], details[2], details[3], Integer.parseInt(details[4]), details[5], details[6]));
                    else
                        shop_orders.get(index).getProduse().add(new Product(details[1], Float.parseFloat(details[2]), details[3], details[4], Integer.parseInt(details[5]), details[6]));
                else {
                    index++;
                    String[] t = details[0].split(":");
                    String[] u = details[1].split(":");
                    String[] a = details[2].split(":");
                    String[] c = details[3].split(":");
                    String[] s = details[4].split(":");
                    String[] m = details[5].split(":");
                    String[] l = details[6].split(":");
                    shop_orders.add(new Order(Long.parseLong(t[1]), u[1], a[1], Float.parseFloat(c[1]), s[1], m[1], new ArrayList<AllProducts>(),l[1]));
                }
            }
            if(shop_orders.isEmpty()) lv.getItems().add("Nu ati primit nici o comanda!");
            else for (Order o : shop_orders)
                if(o.getMagazin().equals(shop_username)) {
                    String produse = "";
                    for (AllProducts p : o.getProduse())
                        if (p.getObject().equals("ANIMAL"))
                            produse = produse + " " + ((Animal) p).getNumeRasa();
                        else produse = produse + " " + ((Product) p).getNume();
                    lv.getItems().add("TIME: " + o.getTime() + "   NUME: " + o.getNume() + "   ADRESA: " + o.getAdresa() + "   COST: " + o.getCost() + "   STARE: " + (o.getStare().equals("TRIMISA")?"PRIMITA":o.getStare()) + "   MAGAZIN: " + o.getMagazin() + "   TIMP_LIVRARE: " + o.getTimpLivrare() + "   OBIECTE: " + produse);
                }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void editOrder() {
        String selectedItem = lv.getSelectionModel().getSelectedItem();
        if (selectedItem == null) message("Error!","Nu ati selectat nici o comanda!");
        else {
            String[] details = selectedItem.split(" ");
            int index = 0;
            for (Order o : shop_orders)
                if (details[1].equals(Long.toString(o.getTime()))&&details[5].equals(o.getNume()))
                    index = shop_orders.indexOf(o);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditeazaComanda.fxml"));
                Parent fxml = loader.load();
                Stage stage = new Stage();
                EditeazaComanda controller = loader.getController();
                controller.set(stage,shop_username,shop_adress,all_products,shop_orders,lv,index);
                stage.setTitle("Editare produs");
                stage.setScene(new Scene(fxml));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    public void listaProduse(javafx.event.ActionEvent actionEvent) {
        try {
            if (tip.equals("petshop")) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/ListaProdusePetshop.fxml"));
                Parent parent = loader.load();
                Scene scene = new Scene(parent);
                ListaProdusePetshop controller = loader.getController();
                controller.set(shop_username,shop_adress, all_products);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
            else{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/ListaAnimaleCentru.fxml"));
                Parent parent = loader.load();
                Scene scene = new Scene(parent);
                ListaAnimaleCentru controller = loader.getController();
                controller.set(shop_username,shop_adress, all_products);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
