import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PrincipalaAnimale{
    private String client_username;
    private  String client_adress;
    private ArrayList<AllProducts> all_products = new ArrayList<AllProducts>();
    private ArrayList<AllProducts> cart;
    @FXML
    private ListView<String> lv = new ListView<String>();
    @FXML
    private RadioButton animale;
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
    private int az_state=0,dn_state=0;

    public void set(boolean az_selected,int az_state,String client_username,String client_adress,ArrayList<AllProducts> cart)
    {
        this.client_username=client_username;
        this.client_adress=client_adress;
        this.cart=cart;
        if(az_selected&&az_state==1) {
            az.setSelected(true);
            filt_and_sort();
        }
        else if(az_selected&&az_state==2) {
            this.az_state = 1;
            filt_and_sort();
        }
        try {
            animale.setSelected(true);
            all_products.clear();
            Scanner sc = new Scanner(new File("src/main/produse.txt"));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] details = line.split(" ");
                if(details[0].equals("ANIMAL"))
                    all_products.add(new Animal(details[1], details[2], details[3], Integer.parseInt(details[4]), details[5], details[6]));
            }
            for (AllProducts p : all_products)
                lv.getItems().add(p.toLV());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addToCart() {
        String selectedItem = lv.getSelectionModel().getSelectedItem();
        if (selectedItem == null) mesaj("Eroare!","Nu ati selectat un produs!");
        else {
            String[] details = selectedItem.split(" ");
            int index = 0;
            for (AllProducts p : all_products)
                if (p.getObject().equals("PRODUCT")) {
                    if (((Product) p).getNume().equals(details[1])) index = all_products.indexOf(p);
                } else if (((Animal) p).getNumeRasa().equals(details[1]))
                    index = all_products.indexOf(p);
            if (all_products.get(index).getDisponibilitate().equals("INDISPONIBIL")) {
                mesaj("Eroare!", "Produsul e indisponibil momentan!");
            }
            else {
                if (cart.isEmpty()) {
                    cart.add(all_products.get(index));
                    mesaj("Mesaj", "Produsul a fost adaugat in cos");
                }
                else {
                    AllProducts p0 = cart.get(0);
                    AllProducts pn = all_products.get(index);
                    if (p0.getObject().equals("ANIMAL") && pn.getObject().equals("ANIMAL")&&((Animal) p0).getCentru().equals(((Animal) pn).getCentru()))
                        if (cart.contains(pn)) mesaj("Eroare!", "Animalul este deja adaugat!");
                        else {
                            cart.add(pn);
                            mesaj("Mesaj!", "Produsul a fost adaugat in cos");
                        }
                    else if ((p0.getObject().equals("PRODUCT")) && pn.getObject().equals("PRODUCT")&&((Product) p0).getMagazin().equals(((Product) pn).getMagazin()))
                    {
                        int nr = 1;
                        for (AllProducts p : cart)
                            if (((Product) p).getNume().equals(((Product) pn).getNume()))
                                nr++;
                        if (nr > ((Product) pn).getCantitate())mesaj("Eroare!", "Stocul este insuficient pentru cerere!");
                        else {
                            cart.add(pn);
                            mesaj("Mesaj!", "Produsul a fost adaugat in cos");
                        }
                    }
                    else mesaj("Eroare!", "Produsule din cos nu sunt\nde la acelasi magazin!");
                }
            }
        }
    }

    public void mesaj(String titlu,String mesaj) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Message.fxml"));
            Parent fxml = loader.load();
            Stage stage = new Stage();
            Message controller = loader.getController();
            controller.set(mesaj,stage);
            stage.setTitle(titlu);
            stage.setScene(new Scene(fxml));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void filt_and_sort() {
        String[] c={"FEMININ","MASCULIN","PISICI","CAINI","PAPAGALI","PESTISORI","HAMSTERI"};
        boolean[] f ={false,false,false,false,false,false,false},s={false,false,false,false};
        try {
            if(fem.isSelected())f[0]=true;
            if(masc.isSelected())f[1]=true;
            if(pisici.isSelected())f[2]=true;
            if(caini.isSelected())f[3]=true;
            if(papagali.isSelected())f[4]=true;
            if(pestisori.isSelected())f[5]=true;
            if(hamsteri.isSelected())f[6]=true;
            if(az.isSelected()&&az_state==0)
            {
                if(dn.isSelected())
                {
                    dn.setSelected(false);
                    dn_state =0;
                    dn.setText("Pret (crescator)");
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
        ArrayList<AllProducts> filt = new ArrayList<AllProducts>();
        ArrayList<AllProducts> filt_and_sort = new ArrayList<AllProducts>();
        lv.getItems().clear();
        if(f[0]||f[1]||f[2]||f[3]||f[4]||f[5]||f[6])
            for (AllProducts p : all_products) {
                if (((Animal) p).getSex().equalsIgnoreCase(c[0]) && f[0] || ((Animal) p).getSex().equalsIgnoreCase(c[1]) && f[1] || ((Animal) p).getTip().equalsIgnoreCase(c[2]) && f[2] || ((Animal) p).getTip().equalsIgnoreCase(c[3]) && f[3] || ((Animal) p).getTip().equalsIgnoreCase(c[4]) && f[4] || ((Animal) p).getTip().equalsIgnoreCase(c[5]) && f[5] || ((Animal) p).getTip().equalsIgnoreCase(c[6]) && f[6])
                    filt.add(p);
            }
        else filt.addAll(all_products);
        if(!filt.isEmpty()) {
            if (s[0]||s[1]||s[2]||s[3]) {
                int i, n = filt.size();
                for (i = 0; i < n; i++) {
                    AllProducts aux = filt.get(0);
                    for (AllProducts p : filt)
                        if(s[0]&&((Animal)p).getNumeRasa().toUpperCase().compareTo(((Animal)aux).getNumeRasa().toUpperCase())<0 || s[1]&&((Animal)p).getNumeRasa().toUpperCase().compareTo(((Animal)aux).getNumeRasa().toUpperCase())>0 || s[2]&& ((Animal) p).getDataNasterii()<((Animal) aux).getDataNasterii() || s[2]&& ((Animal) p).getDataNasterii()>((Animal) aux).getDataNasterii())
                            aux=p;
                    filt_and_sort.add(aux);
                    filt.remove(aux);
                }
            }
            else filt_and_sort.addAll(filt);
            for (AllProducts p : filt_and_sort)
                lv.getItems().add(p.toLV());
        }
    }

    public void principala_produse(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/PrincipalaProduse.fxml"));
            Parent parent=loader.load();
            Scene scene = new Scene(parent);
            PrincipalaProduse controller=loader.getController();
            controller.set(az.isSelected(),az_state,client_username,client_adress,cart);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void principala(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Principala.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Principala controller = loader.getController();
            if(animale.isSelected())
                controller.set(false, 0,client_username,client_adress, cart);
            else controller.set(az.isSelected(), az_state, client_username,client_adress,cart);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void magazine(javafx.event.ActionEvent actionEvent) {
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
    public void comenzi(javafx.event.ActionEvent actionEvent) {
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
    public void cos(javafx.event.ActionEvent actionEvent) {
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