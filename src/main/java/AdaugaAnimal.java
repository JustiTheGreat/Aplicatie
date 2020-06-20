import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AdaugaAnimal {
    private String shop_username;
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
    private int az_state,dn_state;
    private Stage stage;
    @FXML
    private TextField nume;
    @FXML
    private TextField data;
    @FXML
    private ChoiceBox tip;
    @FXML
    private ChoiceBox sex;

    public void set(Stage stage, String shop_username,ArrayList<AllProducts> all_products, ListView<String> lv, RadioButton fem, RadioButton masc, RadioButton pisici, RadioButton caini, RadioButton papagali, RadioButton pestisori, RadioButton hamsteri, RadioButton az, RadioButton dn,int az_state,int dn_state) {
    }
}
