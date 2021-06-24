package Project_2;
 
import java.io.File;
import java.io.FileInputStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GUI extends Application {
    public static void main(String args[]) {
        GUI.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader();
        File fxmlPath = new File("./Project_2/UI.fxml");
        // Begin Attempt Load
        try {
            FileInputStream fxmlStream = new FileInputStream(fxmlPath);
            FlowPane root = (FlowPane) loader.load(fxmlStream);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Test");
            primaryStage.show();
        }

        catch (Exception e) {
            System.out.println("Error Loading FXML");
            System.out.println(System.getProperty("user.dir"));
            e.printStackTrace();
            Platform.exit();
        } // End Attempt Load
    }
}