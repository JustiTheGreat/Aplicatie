package TotPanaLaAutentificare;

import MyConnection.Utillity;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Start extends Utillity {

    public void deschideInregistrare (MouseEvent mouseEvent) throws IOException {
        Parent fxml= FXMLLoader.load(getClass().getResource("/Alegere.fxml"));
        Scene loginScene=new Scene(fxml);
        Stage window=(Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    public void deschideAutentificare (MouseEvent mouseEvent) throws IOException {
        Parent fxml= FXMLLoader.load(getClass().getResource("/Autentificare.fxml"));
        Scene signupScene=new Scene(fxml);
        Stage window=(Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        window.setScene(signupScene);
        window.show();
    }
}
