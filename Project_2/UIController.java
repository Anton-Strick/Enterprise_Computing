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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

public class UIController {

    private enum Credential {
        username(0), password(1), driver(2), database(3);

        private final int index;
        
        private Credential(int index) { this.index = index; }

        public int index() { return index; }
    }

    private String[] creds = new String[4]; // Credentials for DB Connection

    private ObservableList<String> databases = 
                FXCollections.observableArrayList("jdbc:mysql/localhost/databaseName");

    private ObservableList<String> drivers = 
                FXCollections.observableArrayList("com.mysql.cj.jdbc.Driver");

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

        
    }

    //============================ SQL Output Terminal ===========================//
    @FXML
    private TextArea sqlOutArea;
    //========================== End SQL Output Terminal =========================//

    @FXML
    private void initialize() {
        selectDriver.setItems(drivers);
        selectDataBase.setItems(databases);
        sqlCommandArea.setEditable(false);
        dbStatusField.setEditable(false);
    }
}
