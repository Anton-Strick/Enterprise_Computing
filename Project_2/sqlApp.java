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
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.cj.jdbc.MysqlDataSource;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class sqlApp extends Application {

    private String propertiesPath = "./Project_2/database.properties";

    private Stage primaryStage;
    private MysqlDataSource operationLogDB;
    private Connection operationLogConn;

    private DatabaseManager operationLogManager;

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

        operationLogManager = 
            new DatabaseManager(operationLogDB, operationLogConn);
        
        System.out.println(operationLogManager.connectTo(propertiesPath));
    }

    public void setUpdate() {

    }

    public void getUpdate() {

    }
}