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
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class sqlApp extends Application {

    private Stage primaryStage;
    private MysqlDataSource operationLogDB;
    private Connection operationLogConn;

    public static void main(String args[]) {
        sqlApp.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        UIController uiControl = new UIController();

        primaryStage.setScene(new Scene(uiControl));
        primaryStage.setTitle("SQL Application");
        primaryStage.show();

        System.out.println(connectTo(operationLogDB, operationLogConn, 
                           "Project_2/database.properties"));
    }

    public void setUpdate() {

    }

    public void getUpdate() {

    }

    /**
     * Attempts to establish a connection to the given database using the attached
     * properties file 
     * @param db the data source to be used
     * @param path the path to the .properties file required
     */
    public String connectTo(MysqlDataSource db, Connection c, String path) {
        Properties properties = new Properties();
        FileInputStream propertiesFile;
        try {
            //--------------------- Load Properties File --------------------//
            propertiesFile = new FileInputStream(path);
            properties.load(propertiesFile);
            propertiesFile.close();

            //--------------------- Configure DataSource --------------------//
            db = new MysqlDataSource();
            
            db.setURL(properties.getProperty("MYSQL_DB_URL"));
            db.setUser(properties.getProperty("MYSQL_USERNAME"));
            db.setPassword(properties.getProperty("MYSQL_PASSWORD"));

            //-------------------- Connect to DataSource --------------------//
            c = db.getConnection();
            return "SUCCESS: CONNECTED TO " + db.getUrl();
        }

        catch (Exception e) {
            return "ERROR:  " + e.toString();
        }
    }
}