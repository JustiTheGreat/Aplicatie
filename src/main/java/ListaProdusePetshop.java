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

public class ListaProdusePetshop {
    private String shop_username;
    private String shop_adress;
    private ArrayList<AllProducts> all_products;
    @FXML
    private ListView<String> lv = new ListView<String>();
    @FXML
    private RadioButton hrana;
    @FXML
    private RadioButton ingrijire;
    @FXML
    private RadioButton jucarii;
    @FXML
    private RadioButton az;
    @FXML
    private RadioButton price;
    private int az_state=0, price_state =0;
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
                if (p.getObject().equals("PRODUCT")&&((Product)p).getMagazin().equals(shop_username))
                        lv.getItems().add(p.toLV());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdaugaProdus.fxml"));
            Parent fxml = loader.load();
            Stage stage = new Stage();
            AdaugaProdus controller = loader.getController();
            controller.set(stage,shop_username,all_products,lv,hrana,ingrijire,jucarii,az,price,az_state,price_state);
            stage.setTitle("Adaugare produs");
            stage.setScene(new Scene(fxml));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editProduct() {
        String selectedItem = lv.getSelectionModel().getSelectedItem();
        if (selectedItem == null) message("Error!","Nu ati selectat nici un produs!");
        else {
            String[] details = selectedItem.split(" ");
            int index = 0;
            for (AllProducts p : all_products)
                if (p.getObject().equals("PRODUCT")&&((Product)p).getNume().equals(details[1])) index = all_products.indexOf(p);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditeazaProdus.fxml"));
                Parent fxml = loader.load();
                Stage stage = new Stage();
                EditeazaProdus controller = loader.getController();
                controller.set(stage,shop_username,all_products,lv,hrana,ingrijire,jucarii,az,price,az_state,price_state,index);
                stage.setTitle("Editare produs");
                stage.setScene(new Scene(fxml));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteProduct() {
        String selectedItem = lv.getSelectionModel().getSelectedItem();
        if (selectedItem == null) message("Error!","Nu ati selectat nici un produs!");
        else {
            String[] details = selectedItem.split(" ");
            int index=0;
            for (AllProducts p : all_products)
                if (p.getObject().equals("PRODUCT")&&((Product)p).getNume().equals(details[0])) index=all_products.indexOf(p);
            all_products.remove(index);
            write();
            filt_and_sort();
        }
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
                    if (((Product) p).getCategorie().equalsIgnoreCase(c[0]) && f[0]
                            || ((Product) p).getCategorie().equalsIgnoreCase(c[1]) && f[1]
                            || ((Product) p).getCategorie().equalsIgnoreCase(c[2]) && f[2])
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