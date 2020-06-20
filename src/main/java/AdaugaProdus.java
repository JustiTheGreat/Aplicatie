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

public class AdaugaProdus{
    private String shop_username;
    private ArrayList<AllProducts> all_products;
    private ListView<String> lv;
    private RadioButton hrana;
    private RadioButton ingrijire;
    private RadioButton jucarii;
    private RadioButton az;
    private RadioButton price;
    private int az_state,price_state;
    private Stage stage;
    @FXML
    private TextField nume;
    @FXML
    private TextField pret;
    @FXML
    private ChoiceBox categorie;
    @FXML
    private TextField cantitate;

    public void set(Stage stage, String shop_username,ArrayList<AllProducts> all_products, ListView<String> lv, RadioButton hrana, RadioButton ingrijire, RadioButton jucarii, RadioButton az, RadioButton price,int az_state,int price_state) {
        this.stage = stage;
        this.shop_username = shop_username;
        this.all_products = all_products;
        this.lv = lv;
        this.hrana = hrana;
        this.ingrijire = ingrijire;
        this.jucarii = jucarii;
        this.az=az;
        this.price=price;
        categorie.getItems().addAll("HRANA","INGRIJIRE","JUCARII");
        if (az.isSelected())
        {
            this.price.setSelected(false);
            this.price_state=0;
            if (az_state == 1) {
                this.az.setSelected(true);
                this.az_state = 0;
            } else if (az_state == 2) {
                this.az.setSelected(false);
                this.az_state = 1;
            }
        }
        else if (price.isSelected())
        {
            this.az.setSelected(false);
            this.az_state=0;
            if (price_state == 1) {
                this.price.setSelected(true);
                this.price_state = 0;
            } else if (price_state == 2) {
                this.price.setSelected(false);
                this.price_state = 1;
            }
        }
        else
        {
            this.az.setSelected(false);
            this.az_state=0;
            this.price.setSelected(false);
            this.price_state=0;
        }
    }

    public boolean isFloat(String s) {
        try{
            Float.parseFloat(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isInt(String s) {
        try{
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void close()
    {
        stage.close();
    }

    public void addLV () {
        try {
            int ok = 1;
            for (AllProducts p : all_products)
                if (p.getObject().equals("PRODUCT") && ((Product) p).getNume().equals(nume.getText()))
                    ok = 0;
            if (ok == 1 && isFloat(pret.getText()) && isInt(cantitate.getText()) && Float.parseFloat(pret.getText()) >= 0 && Integer.parseInt(cantitate.getText()) >= 0 && !nume.getText().isEmpty() && !pret.getText().isEmpty() && categorie != null && !cantitate.getText().isEmpty()) {
                all_products.add(new Product(nume.getText(), Float.parseFloat(pret.getText()), categorie.getValue().toString(), shop_username, Integer.parseInt(cantitate.getText()), Integer.parseInt(cantitate.getText()) == 0 ? "INDISPONIBIL" : "DISPONIBIL"));
                write();
                filt_and_sort();
                stage.close();
            } else {
                nume.setText("");
                pret.setText("");
                categorie.setValue("");
                cantitate.setText("");
                message("Eroare!", "Date introduse gresit!");
            }
        }catch(Exception e){message("Eroare!", "Categorie neselectata!");}
    }
    public void write() {
        try {
            FileWriter myWriter = new FileWriter("src/main/produse.txt", false);
            for (AllProducts p : all_products)
                myWriter.write(p.getObject() + " " + p.toString() + "\n");
            myWriter.close();
        } catch (IOException e) {
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

    public void filt_and_sort() {
        String[] c ={"HRANA","INGRIJIRE","JUCARII"};
        boolean[] f={false,false,false},s ={false,false,false,false};
        try {
            if (hrana.isSelected()) f[0] = true;
            if (ingrijire.isSelected()) f[1] = true;
            if (jucarii.isSelected()) f[2] = true;
            if(az.isSelected()&&az_state==0)
            {
                if(price.isSelected())
                {
                    price.setSelected(false);
                    price_state =0;
                    price.setText("Pret (crescator)");
                }
                s[0]=true;
                az_state=1;
            }
            else if(az.isSelected()&&az_state==1)
                s[0]=true;
            else if(!az.isSelected()&&az_state==1)
            {
                az.setText("Alfabetic (Z-A)");
                az.setSelected(true);
                s[1]=true;
                az_state=2;
            }
            else if(az.isSelected()&&az_state==2)
                s[1]=true;
            else if(!az.isSelected()&&az_state==2)
            {
                az.setText("Alfabetic (A-Z)");
                az_state=0;
            }

            if(price.isSelected()&& price_state ==0)
            {
                if(az.isSelected())
                {
                    s[0]=false;s[1]=false;
                    az.setSelected(false);
                    az_state=0;
                    az.setText("Alfabetic (A-Z)");
                }
                s[2]=true;
                price_state =1;
            }
            else if(price.isSelected()&&price_state==1)
                s[2]=true;
            else if(!price.isSelected()&& price_state ==1)
            {
                price.setText("Pret (descrescator)");
                price.setSelected(true);
                s[3]=true;
                price_state =2;
            }
            else if(price.isSelected()&&price_state==2)
                s[3]=true;
            else if(!price.isSelected()&& price_state ==2)
            {
                price.setText("Pret (crescator)");
                price_state =0;
            }
        } catch (Exception e) { }
        ArrayList<Product> filt = new ArrayList<Product>();
        ArrayList<Product> filt_and_sort = new ArrayList<Product>();
        lv.getItems().clear();
        if(f[0]||f[1]||f[2]) {
            for (AllProducts p : all_products)
                if (p.getObject().equals("PRODUCT") && ((Product) p).getMagazin().equals(shop_username))
                    if (((Product) p).getCategorie().equalsIgnoreCase(c[0]) && f[0] || ((Product) p).getCategorie().equalsIgnoreCase(c[1]) && f[1] || ((Product) p).getCategorie().equalsIgnoreCase(c[2]) && f[2])
                        filt.add(((Product)p));
        }
        else
            for (AllProducts p : all_products)
                if (p.getObject().equals("PRODUCT") && ((Product) p).getMagazin().equals(shop_username))
                    filt.add(((Product)p));
        if(!filt.isEmpty()) {
            if (s[0]||s[1]||s[2]||s[3]) {
                int i,n=filt.size();
                for (i = 0; i < n; i++) {
                    Product aux = filt.get(0);
                    for (Product p : filt)
                        if(s[0] && p.getNume().toUpperCase().compareTo(aux.getNume().toUpperCase())<0 || s[1] && p.getNume().toUpperCase().compareTo(aux.getNume().toUpperCase())>0 || s[2] && p.getPret() < aux.getPret() || s[3] && p.getPret() > aux.getPret())
                            aux = p;
                    filt_and_sort.add(aux);
                    filt.remove(aux);
                }
            }
            else filt_and_sort.addAll(filt);
            for (Product p : filt_and_sort)
                lv.getItems().add(p.toLV());
        }
    }
}
