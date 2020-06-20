import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Autentificare {
    private ArrayList<Utilizatori> List=new ArrayList<Utilizatori>();
    @FXML
    private TextField username;
    @FXML
    private PasswordField parola;

    public void  click(MouseEvent mouseEvent) throws IOException {
        if(username.getText().trim().isEmpty()||parola.getText().trim().isEmpty())
            eroare("Completati toate campurile");
        else {
            //Decodificare xml file
            try {
                FileInputStream fis = new FileInputStream("./DateUseriXML.xml");
                XMLDecoder decoder = new XMLDecoder(fis);
                ArrayList list = new ArrayList();
                list = (ArrayList) decoder.readObject();
                List = list;
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }


            int ok=0;
            for (int i = 0; i < List.size()&&ok==0; i++) {
                if (List.get(i).getUsername().equals(username.getText())) {
                    ok = 1;
                    CryptoDecrypto decrypto=new BasicCryptoDecrypto();
                    String dec=new String(decrypto.decrypt(List.get(i).getParola().getBytes()));
                    if (!(dec.equals(parola.getText() ) ) ) {
                        eroare("Parola gresita.");
                        parola.clear();
                        return;
                    }
                    else {
                        if (List.get(i).getCategorie().equals("Client")){
                            try {
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("/Principala.fxml"));
                                Parent parent = loader.load();
                                Scene scene = new Scene(parent);
                                Principala controller = loader.getController();
                                controller.set(false, 0,List.get(i).getUsername(),List.get(i).getJudet(), new ArrayList<AllProducts>());
                                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                                stage.setScene(scene);
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (List.get(i).getCategorie().equals("Centru")){
                            try {
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("/ListaAnimaleCentru.fxml"));
                                Parent parent = loader.load();
                                Scene scene = new Scene(parent);
                                ListaAnimaleCentru controller = loader.getController();
                                controller.set(((Centru)List.get(i)).getNumecentru(),((Centru)List.get(i)).getJudet(), new ArrayList<AllProducts>());
                                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                                stage.setScene(scene);
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (List.get(i).getCategorie().equals("Petshop")){
                            try {
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("/ListaProdusePetshop.fxml"));
                                Parent parent = loader.load();
                                Scene scene = new Scene(parent);
                                ListaProdusePetshop controller = loader.getController();
                                controller.set(((Petshop)List.get(i)).getNumepetshop(),((Petshop)List.get(i)).getJudet(), new ArrayList<AllProducts>());
                                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                                stage.setScene(scene);
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            if (ok == 0) {
                eroare("Username gresit.");
                parola.clear();
                username.clear();
                return;
            }
        }
    }
    public void eroare(String s) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Message.fxml"));
            Parent fxml = loader.load();
            Stage stage = new Stage();
            Message controller = loader.getController();
            controller.set(s,stage);
            stage.setTitle("Error!");
            stage.setScene(new Scene(fxml));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
