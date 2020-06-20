import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EditeazaComanda {
    private String shop_username;
    private String shop_adress;
    private ArrayList<AllProducts> all_products;
    private ArrayList<Order> shop_orders = new ArrayList<Order>();
    @FXML
    private ListView<String> lv = new ListView<String>();
    private Stage stage;
    private int index;
    @FXML
    private ChoiceBox stare;

    public void set(Stage stage, String shop_username, String shop_adress, ArrayList<AllProducts> all_products, ArrayList<Order> shop_orders, ListView<String> lv, int index) {
    }
}
