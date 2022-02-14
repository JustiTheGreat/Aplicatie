package MyConnection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Message {
    private Stage stage;
    @FXML
    private Text message;
    public void set(String message,Stage stage) {
        this.message.setText(message);
        this.stage=stage;
    }

    public void problem()
    {
        stage.close();
    }
}
