package Project_2;

/*
    Name: J. Anton Strickland
    Course: CNT 4714 Summer 2021
    Assignment title: Project 2 - Two-Tier Client-Server Application Development With
                                  MySQL and JDBC
    Date: June 27, 2021

    Class: sqlApp
    Description: Contains initialization, back-end logic, and methods to retrieve
                 and deliver information from a MySQL database to other classes.
*/

import java.io.File;
import java.io.FileInputStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class sqlApp extends Application {

    private Stage primaryStage;
    private FlowPane mainLayout;
    public static void main(String args[]) {
        sqlApp.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SQL Application");
        showSQLUI();
    }

    private void showSQLUI() {
        FXMLLoader loader = new FXMLLoader();
        File sqlUI = new File("./Project_2/UI.fxml");

        // Begin Attempt Load
        try {
            FileInputStream fxmlStream = new FileInputStream(sqlUI);
            mainLayout = (FlowPane) loader.load(fxmlStream);
            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
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