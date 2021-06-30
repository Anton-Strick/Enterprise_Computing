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
import java.sql.ResultSet;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.scene.control.TextArea;

import com.mysql.cj.jdbc.MysqlDataSource;

public class UIController extends VBox {

    private DatabaseManager clientDBManager;

    private Connection sqlClient;

    private MysqlDataSource dataSource;

    private String[] creds = new String[4]; // Credentials for DB Connection

    private sqlApp app;

    private ObservableList<String> databaseURLs = 
                FXCollections.observableArrayList("jdbc:mysql://127.0.0.1:3306/project2");

    private ObservableList<String> drivers = 
                FXCollections.observableArrayList("com.mysql.cj.jdbc.MysqlDataSource");

    private ObservableList<ObservableList> data;

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

        clientDBManager = new DatabaseManager(dataSource, sqlClient);
    }

    private enum Credential {
        username(0), password(1), driver(2), database(3);

        private final int index;
        
        private Credential(int index) { this.index = index; }

        public int index() { return index; }
    }

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
        try {
            Class.forName(selectDriver.getValue());
        }
        
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            dbStatusField.setText("ERROR:  " + e.toString());
            return;
        }

        dbStatusField.setText(clientDBManager.connectTo(selectDataBase.getValue(),
                                                        userNameField.getText(),
                                                        passwordField.getText()));
        // Update connection
        this.sqlClient = clientDBManager.getConnection();
        this.dataSource = clientDBManager.getDatabase();
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
        selectDataBase.setItems(databaseURLs);
        sqlCommandArea.setEditable(true);
        dbStatusField.setEditable(false);
    }

    @FXML
    private void clearCommand() {
        sqlCommandArea.clear();
    }

    @FXML
    private void executeSQLCommand() {
        //------------------------------- Read Query -----------------------------//
        String sqlQuery = sqlCommandArea.getText();
        if (sqlQuery == null) {
            return; // no Command
        }
        //------------------------------ Attempt Query ---------------------------//
        data = FXCollections.observableArrayList(); // To be written to table
        try {
            ResultSet results = this.sqlClient.createStatement().executeQuery(sqlQuery);

            // Dynamically allocate column headers, see
            // https://blog.ngopal.com.np/2011/10/19/dyanmic-tableview-data-from-database/
            for (int i = 0; i < results.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = 
                    new TableColumn(results.getMetaData().getColumnName(i+1));
                
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                sqlTableView.getColumns().addAll(col);
            }

            // Add resulting data to 'data'
            while (results.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= results.getMetaData().getColumnCount(); i++) {
                    row.add(results.getString(i));
                }
                data.add(row);
            }

            // Post results to UI
            sqlTableView.setItems(data);
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
