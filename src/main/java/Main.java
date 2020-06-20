import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent fxml= FXMLLoader.load(getClass().getResource("/Start.fxml"));
        Scene startScene=new Scene(fxml);
        primaryStage.setScene(startScene);
        primaryStage.setTitle("Aplicatie: Petshop - Centru de adoptie animale");
        primaryStage.show();
    }
}
