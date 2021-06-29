package Project_2;
/*
    Name: J. Anton Strickland
    Course: CNT 4714 Summer 2021
    Assignment title: Project 2 - Two-Tier Client-Server Application Development With
                                  MySQL and JDBC
    Date: June 27, 2021

    Class: UIController
    Description: Contains the elementIDs and functions used by the javaFX UI
                 Designed using Scene Builder. Is used by sqlApp.java to report
                 user inputs, and receives information via string from sqlApp
                 to display in UI windows.
*/

import java.io.IOException;
import java.sql.Connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.TextArea;

import com.mysql.cj.jdbc.MysqlDataSource;

public class UIController extends FlowPane {

    public UIController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UI.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();            
        } 
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private enum Credential {
        username(0), password(1), driver(2), database(3);

        private final int index;
        
        private Credential(int index) { this.index = index; }

        public int index() { return index; }
    }

    private Connection sqlClient;

    private String[] creds = new String[4]; // Credentials for DB Connection

    private ObservableList<String> databases = 
                FXCollections.observableArrayList("jdbc:mysql/localhost/databaseName");

    private ObservableList<String> drivers = 
                FXCollections.observableArrayList("com.mysql.cj.jdbc.MysqlDataSource");

    private ObservableList<ObservableList> data;

    //========================== Database Information Pane =======================//
    @FXML
    private ComboBox<String> selectDriver;

    @FXML
    private ComboBox<String> selectDataBase;

    @FXML
    private TextField userNameField;

    @FXML
    private TextField passwordField;
    //======================= End Database Information Pane ======================//

    //============================= SQL Command Input ============================//
    @FXML
    private TextArea sqlCommandArea;

    @FXML
    private Button clearCommandBtn;

    @FXML
    private Button executeCommandBtn;
    //=========================== End SQL Command Input ==========================//
    
    //========================= Database Connection Pane =========================//
    @FXML
    private TextField dbStatusField;

    @FXML
    private Button connectBtn;
    @FXML
    private void tryConnect() {
        // Get credentials from info panel
        this.creds[Credential.username.index()] 
            = userNameField.getText();
        this.creds[Credential.password.index()]
            = passwordField.getText();
        this.creds[Credential.driver.index()]
            = selectDriver.getValue();
        this.creds[Credential.database.index()]
            = selectDataBase.getValue();

        try {
            Class.forName(this.creds[Credential.driver.index()]);
        }
        
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error loading driver: Does not Exist");
        }
    }

    //============================ SQL Output Terminal ===========================//
    @FXML
    private TableView sqlTableView;

    @FXML
    private Button clearTableBtn;
    //========================== End SQL Output Terminal =========================//

    @FXML
    private void initialize() {
        selectDriver.setItems(drivers);
        selectDataBase.setItems(databases);
        sqlCommandArea.setEditable(true);
        dbStatusField.setEditable(false);
    }

    @FXML
    private void clearCommand() {
        sqlCommandArea.clear();
    }

    @FXML
    private void executeSQLCommand() {
        data = FXCollections.observableArrayList();
        String sqlQuery = sqlCommandArea.getText();

        try {

        }

        catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: SQL COMMAND FAIL");
        }

    }

    @FXML
    private void clearQueryResults() {
        sqlTableView.getItems().clear();
    }
}
