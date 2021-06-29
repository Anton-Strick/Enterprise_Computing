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

    public static void main(String args[]) {
        sqlApp.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        UIController uiControl = new UIController();

        primaryStage.setScene(new Scene(uiControl));
        primaryStage.setTitle("SQL Application");
        primaryStage.show();
    }

    public void setUpdate() {

    }

    public void getUpdate() {

    }

}