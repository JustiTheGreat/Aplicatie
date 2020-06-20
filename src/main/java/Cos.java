import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Cos {
    private String client_username;
    private String client_adress;
    private ArrayList<AllProducts> cart;
    @FXML
    private ListView<String> lv = new ListView<String>();
    @FXML
    private Text cost;
    public void set(String client_username,String client_adress,ArrayList<AllProducts> cart) {
        this.client_username=client_username;
        this.client_adress=client_adress;
        this.cart=cart;
        if(this.cart.isEmpty())
        {
            lv.getItems().add("Cosul dumneavoastra este gol!");
            cost.setText(Float.toString(price()));
        }
        else reloadLV();
    }
    public Float price()
    {
        Float s= Float.valueOf(0);
        for (AllProducts p : cart)
            if(p.getObject().equals("PRODUCT"))s=s+((Product)p).getPret();
        return s;
    }
    public void reloadLV()
    {
        lv.getItems().clear();
        for (AllProducts p : cart)
            lv.getItems().add(p.toLV());
        if(cart.isEmpty())lv.getItems().add("Cosul dumneavoastra este gol!");
        cost.setText(Float.toString(price()));
    }
    public void proceedOrder() {
        if (cart.isEmpty()) mesaj("Eroare!", "Cosul dumeavoastra e gol!");
        else
            try {
                Date date=new Date();
                FileWriter myWriter = new FileWriter("src/main/comenzi.txt", true);
                myWriter.write("TIME:"+date.getTime()+" NUME:"+client_username+" ADRESA:"+client_adress+" COST:"+price()+" STARE:TRIMISA MAGAZIN:"+ (cart.get(0).getObject().equals("ANIMAL")?((Animal)cart.get(0)).getCentru():((Product)cart.get(0)).getMagazin())+" TIMP_LIVRARE:NESPECIFICAT\n");
                for (AllProducts p : cart)
                    myWriter.write(p.getObject() + " " + p.toString() + "\n");
                myWriter.close();
                mesaj("Mesaj", "Comanda dumneavoastra a\n fost trimisa!");
                cart.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    public void deleteProduct() {
        String selectedItem = lv.getSelectionModel().getSelectedItem();
        if (selectedItem == null) mesaj("Error!","Nu ati selectat un produs!");
        else {
            String[] details = selectedItem.split(" ");
            int index=0;
            for (AllProducts p : cart)
                if (p.getObject().equals("PRODUCT")&&((Product)p).getNume().equals(details[0])) index=cart.indexOf(p);
            cart.remove(index);
            reloadLV();
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
