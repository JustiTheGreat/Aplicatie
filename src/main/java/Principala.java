import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
            sort();
        }
        else if(az_selected&&az_state==2) {
            this.az_state = 1;
            sort();
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
    public void sort() {
        boolean[] s ={false,false};
        try {
            if(az.isSelected()&&az_state==0)
            {
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
        } catch (Exception e) { }
        ArrayList<AllProducts> init = new ArrayList<AllProducts>();
        ArrayList<AllProducts> sort = new ArrayList<AllProducts>();
        lv.getItems().clear();
        init.addAll(all_products);
        if(!init.isEmpty()) {
            if (s[0] || s[1]) {
                int i, n = init.size();
                for (i = 0; i < n; i++) {
                    AllProducts aux = init.get(0);
                    for (AllProducts p : init)
                        if(s[0] && (p.getObject().equals("ANIMAL")?((Animal) p).getNumeRasa():((Product)p).getNume()).toUpperCase().compareTo((aux.getObject().equals("ANIMAL")?((Animal) aux).getNumeRasa():((Product)aux).getNume()).toUpperCase()) < 0
                                || s[1] && (p.getObject().equals("ANIMAL")?((Animal) p).getNumeRasa():((Product)p).getNume()).toUpperCase().compareTo((aux.getObject().equals("ANIMAL")?((Animal) aux).getNumeRasa():((Product)aux).getNume()).toUpperCase()) > 0)
                                aux=p;
                    sort.add(aux);
                    init.remove(aux);
                }
            }
            else sort.addAll(init);
            for (AllProducts p : sort)
                lv.getItems().add(p.toLV());
        }
    }

    public void principala_animale(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/PrincipalaAnimale.fxml"));
            Parent parent=loader.load();
            Scene scene = new Scene(parent);
            PrincipalaAnimale controller=loader.getController();
            controller.set(az.isSelected(),az_state,client_username,client_adress,cart);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
            controller.set(false, 0,client_username,client_adress, cart);
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

