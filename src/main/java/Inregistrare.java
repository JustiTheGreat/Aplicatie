import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;

public class Inregistrare {
    private ArrayList<Utilizatori> List=new ArrayList<Utilizatori>();
    @FXML
    private TextField nume;
    @FXML
    private TextField prenume;
    @FXML
    private TextField judet;
    @FXML
    private TextField localitate;
    @FXML
    private TextField strada;
    @FXML
    private TextField numar;
    @FXML
    private TextField telefon;
    @FXML
    private TextField username;
    @FXML
    private TextField parola;

    public void click(MouseEvent mouseEvent) throws IOException {
        if(
           nume.getText().trim().isEmpty()|| prenume.getText().trim().isEmpty()|| judet.getText().trim().isEmpty()||
           localitate.getText().trim().isEmpty()|| strada.getText().trim().isEmpty()|| numar.getText().trim().isEmpty()||
           telefon.getText().trim().isEmpty()|| username.getText().trim().isEmpty()|| parola.getText().trim().isEmpty()
          )
            eroare("Completati toate campurile");
        else{
            //Decodificare xml file
            try{
                FileInputStream fis = new FileInputStream("./DateUseriXML.xml");
                XMLDecoder decoder = new XMLDecoder(fis);
                ArrayList list = new ArrayList();
                list = (ArrayList) decoder.readObject();
                List =list;
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

            CryptoDecrypto crypto=new BasicCryptoDecrypto();
            String enc=new String(crypto.encrypt(parola.getText().getBytes()));

            Client C = new Client(nume.getText(),prenume.getText(),judet.getText(),
                                  localitate.getText(),strada.getText(),numar.getText(),
                                  telefon.getText(),username.getText(),enc
                                 );

            for (int i=0; i<List.size();i++){
                if(List.get(i).getUsername().equals(username.getText())  ){
                    eroare("Username-ul ales exista deja.Va rugam, alegeti alt username.");
                    username.clear();
                    return;
                }
            }

            //Codificare xml file
            try{
                List.add(C);
                FileOutputStream fos = new FileOutputStream("./DateUseriXML.xml");
                XMLEncoder encoder = new XMLEncoder(fos);
                encoder.writeObject(List);
                encoder.close();
                fos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Parent fxml = FXMLLoader.load(getClass().getResource("/Start.fxml"));
            Scene loginScene = new Scene(fxml);
            Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.show();
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