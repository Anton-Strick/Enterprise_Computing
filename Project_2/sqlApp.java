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

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class sqlApp extends Application {

    public static void main(String args[]) {
        sqlApp.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        UIController uiControl = new UIController();

        primaryStage.setScene(new Scene(uiControl));
        primaryStage.setTitle("SQL Application");
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(755);
        primaryStage.show();
    }
}