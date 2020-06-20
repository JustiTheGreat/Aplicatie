import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ListaAnimaleCentru {
    private String shop_username;
    private String shop_adress;
    private ArrayList<AllProducts> all_products;
    @FXML
    private ListView<String> lv = new ListView<String>();
    @FXML
    private RadioButton fem;
    @FXML
    private RadioButton masc;
    @FXML
    private RadioButton pisici;
    @FXML
    private RadioButton caini;
    @FXML
    private RadioButton papagali;
    @FXML
    private RadioButton pestisori;
    @FXML
    private RadioButton hamsteri;
    @FXML
    private RadioButton az;
    @FXML
    private RadioButton dn;
    private int az_state=0, dn_state =0;
    public void set(String shop_username,String shop_adress,ArrayList<AllProducts> all_products)
    {
        this.shop_username=shop_username;
        this.shop_adress=shop_adress;
        this.all_products=all_products;
        all_products.clear();
        try {
            Scanner sc = new Scanner(new File("src/main/produse.txt"));
            while (sc.hasNextLine()) {
                String[] details = sc.nextLine().split(" ");
                if(details[0].equals("ANIMAL"))
                    all_products.add(new Animal(details[1], details[2], details[3], Integer.parseInt(details[4]), details[5], details[6]));
                else
                    all_products.add(new Product(details[1], Float.parseFloat(details[2]), details[3], details[4], Integer.parseInt(details[5]), details[6]));
            }
            for (AllProducts p : all_products)
                if (p.getObject().equals("ANIMAL")&&((Animal)p).getCentru().equals(shop_username))
                    lv.getItems().add(p.toLV());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}