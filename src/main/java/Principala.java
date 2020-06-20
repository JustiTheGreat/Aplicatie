import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Principala{
    private String client_username;
    private String client_adress;
    private ArrayList<AllProducts> all_products = new ArrayList<AllProducts>();
    private ArrayList<AllProducts> cart;
    @FXML
    private ListView<String> lv = new ListView<String>();
    @FXML
    private RadioButton az;
    private int az_state=0;
    public void set(boolean az_selected,int az_state,String client_username,String client_adress,ArrayList<AllProducts> cart)
    {
        this.client_username=client_username;
        this.client_adress=client_adress;
        this.cart=cart;
        if(az_selected&&az_state==1) {
            az.setSelected(true);
            //sort();
        }
        else if(az_selected&&az_state==2) {
            this.az_state = 1;
            //sort();
        }
        try {
            all_products.clear();
            Scanner sc = new Scanner(new File("src/main/produse.txt"));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] details = line.split(" ");
                if(details[0].equals("ANIMAL"))
                    all_products.add(new Animal(details[1], details[2], details[3], Integer.parseInt(details[4]), details[5], details[6]));
                else
                    all_products.add(new Product(details[1], Float.parseFloat(details[2]), details[3], details[4], Integer.parseInt(details[5]), details[6]));
            }
            for (AllProducts p : all_products)
                lv.getItems().add(p.toLV());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

