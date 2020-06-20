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

    public void addAnimal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdaugaAnimal.fxml"));
            Parent fxml = loader.load();
            Stage stage = new Stage();
            AdaugaAnimal controller = loader.getController();
            controller.set(stage,shop_username,all_products,lv,fem,masc,pisici,caini,papagali,pestisori,hamsteri,az,dn,az_state,dn_state);
            stage.setTitle("Adaugare produs");
            stage.setScene(new Scene(fxml));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editAnimal() {
        String selectedItem = lv.getSelectionModel().getSelectedItem();
        if (selectedItem == null) message("Error!","Nu ati selectat nici un animal!");
        else {
            String[] details = selectedItem.split(" ");
            int index = 0;
            for (AllProducts p : all_products)
                if (p.getObject().equals("ANIMAL")&&((Animal)p).getNumeRasa().equals(details[1])) index = all_products.indexOf(p);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditeazaAnimal.fxml"));
                Parent fxml = loader.load();
                Stage stage = new Stage();
                EditeazaAnimal controller = loader.getController();
                controller.set(stage,shop_username,all_products,lv,fem,masc,pisici,caini,papagali,pestisori,hamsteri,az,dn,az_state,dn_state,index);
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

    public void filt_and_sort() {
        String[] c ={"FEMININ","MASCULIN","PISICI","CAINI","PAPAGALI","PESTISORI","HAMSTERI"};
        boolean[] f={false,false,false,false,false,false,false},s ={false,false,false,false};
        try {
            if (fem.isSelected()) f[0] = true;
            if (masc.isSelected()) f[1] = true;
            if (pisici.isSelected()) f[2] = true;
            if (caini.isSelected()) f[3] = true;
            if (papagali.isSelected()) f[4] = true;
            if (pestisori.isSelected()) f[5] = true;
            if (hamsteri.isSelected()) f[6] = true;

            if(az.isSelected()&&az_state==0)
            {
                if(dn.isSelected())
                {
                    dn.setSelected(false);
                    dn_state =0;
                    dn.setText("Data nasterii (crescator)");
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

            if(dn.isSelected()&& dn_state ==0)
            {
                if(az.isSelected())
                {
                    s[0]=false;s[1]=false;
                    az.setSelected(false);
                    az_state=0;
                    az.setText("Alfabetic (A-Z)");
                }
                s[2]=true;
                dn_state =1;
            }
            else if(dn.isSelected()&&dn_state==1)
                s[2]=true;
            else if(!dn.isSelected()&& dn_state ==1)
            {
                dn.setText("Data nasterii (descrescator)");
                dn.setSelected(true);
                s[3]=true;
                dn_state =2;
            }
            else if(dn.isSelected()&&dn_state==2)
                s[3]=true;
            else if(!dn.isSelected()&& dn_state ==2)
            {
                dn.setText("Data nasterii (crescator)");
                dn_state =0;
            }
        } catch (Exception e) { }
        ArrayList<Animal> filt = new ArrayList<Animal>();
        ArrayList<Animal> filt_and_sort = new ArrayList<Animal>();
        lv.getItems().clear();
        if(f[0]||f[1]||f[2]||f[3]||f[4]||f[5]||f[6]) {
            for (AllProducts p : all_products)
                if (p.getObject().equals("ANIMAL") && ((Animal) p).getCentru().equals(shop_username))
                    if (((Animal) p).getSex().equalsIgnoreCase(c[0]) && f[0]
                            || ((Animal) p).getSex().equalsIgnoreCase(c[1]) && f[1]
                            || ((Animal) p).getTip().equalsIgnoreCase(c[2]) && f[2]
                            || ((Animal) p).getTip().equalsIgnoreCase(c[3]) && f[3]
                            || ((Animal) p).getTip().equalsIgnoreCase(c[6]) && f[6]
                            || ((Animal) p).getTip().equalsIgnoreCase(c[4]) && f[4]
                            || ((Animal) p).getTip().equalsIgnoreCase(c[5]) && f[5]
                    )
                        filt.add(((Animal)p));
        }
        else
            for (AllProducts p : all_products)
                if (p.getObject().equals("ANIMAL") && ((Animal) p).getCentru().equals(shop_username))
                    filt.add(((Animal)p));
        if(!filt.isEmpty()) {
            if (s[0]||s[1]||s[2]||s[3]) {
                int i,n=filt.size();
                for (i = 0; i < n; i++) {
                    Animal aux = filt.get(0);
                    for (Animal p : filt)
                        if(s[0] && p.getNumeRasa().toUpperCase().compareTo(aux.getNumeRasa().toUpperCase())<0 || s[1] && p.getNumeRasa().toUpperCase().compareTo(aux.getNumeRasa().toUpperCase())>0 || s[2] && p.getDataNasterii() < aux.getDataNasterii() || s[3] && p.getDataNasterii() > aux.getDataNasterii())
                            aux = p;
                    filt_and_sort.add(aux);
                    filt.remove(aux);
                }
            }
            else filt_and_sort.addAll(filt);
            for (Animal p : filt_and_sort)
                lv.getItems().add(p.toLV());
        }
    }
}