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
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TextArea;

import com.mysql.cj.jdbc.MysqlDataSource;

public class UIController extends VBox {

    private DatabaseManager clientDBManager;
    private Connection sqlClient;
    private MysqlDataSource dataSource;
    private boolean isConnected = false;

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

        clientDBManager = new DatabaseManager();
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
        dbStatusField.setText(connectTo(selectDataBase.getValue(),
                                        userNameField.getText(),
                                        passwordField.getText()));
                    
        if (dbStatusField.getText().contains("SUCCESS")) {
            dbStatusField.setStyle("-fx-text-inner-color: green");
            this.isConnected = true;
        }

        else {
            dbStatusField.setStyle("-fx-text-inner-color: red");
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
        
        if (!isConnected) {
            createPopup("No active connection!");
            return;
        }

        //----------------------------- Read SQL Item ----------------------------//
        String sql = sqlCommandArea.getText();
        if (sql == null) {
            return; // no Command
        }

        if (sql.contains("select")) {
            //---------------------------- Attempt Query -------------------------//
            data = FXCollections.observableArrayList(); // To be written to table
            try {
                ResultSet results = this.sqlClient.createStatement().executeQuery(sql);

/* Dynamically allocate column headers, see
   https://blog.ngopal.com.np/2011/10/19/dyanmic-tableview-data-from-database/ */
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

                // Add resulting rows to data
                while (results.next()) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= results.getMetaData().getColumnCount(); i++) {
                        row.add(results.getString(i));
                    }
                    data.add(row);
                }

                // Post results to UI and update log
                sqlTableView.setItems(data);
                clientDBManager.setQuery();
            }

            catch (Exception e) {
                e.printStackTrace();
                createPopup(e.toString());
                System.out.println("ERROR: SQL QUERY FAIL");
            }
        }
        //---------------------------- End Attempt Query -------------------------//

        else {
        //-------------------------- Attempt SQL Command -------------------------//
            try {
                String message = "Updated " +
                this.sqlClient.createStatement().executeUpdate(sql) +
                " rows!";
                createPopup(message);
                clientDBManager.setUpdate();
            }
            
            catch (Exception e) {
                e.printStackTrace();
                createPopup(e.toString());
                System.out.println("ERROR: SQL COMMAND FAIL");
            }
        }
        //-------------------------- End Attempt SQL Command --------------------//
    }

    @FXML
    private void clearQueryResults() {
        sqlTableView.getItems().clear();
    }

    private void createPopup(String message) {
        VBox popupContent = new VBox();
        popupContent.setAlignment(Pos.CENTER);
        popupContent.getChildren().add(new Label("ERROR"));
        TextArea errorMessage = new TextArea(message);
        errorMessage.setWrapText(true);
        errorMessage.setMinWidth(180);
        errorMessage.setPadding(new Insets(10));
        errorMessage.setEditable(false);
        popupContent.getChildren().add(errorMessage);
        popupContent.setMinWidth(200);
        Stage popup = new Stage();
        popup.setScene(new Scene(popupContent));
        popup.setTitle("SQL Error Output");
        popup.show();
    }

    /**
     * Attampts to connect to a database using the following parameters
     * @param url URL to the desired database
     * @param user username to be used to connect to the database
     * @param password password associated with the username passed
     * @return either a SUCCESS with the connected database, or an ERROR with
     *         the exception's string
     */
    public String connectTo(String url, String user, String password) {
        if (dataSource == null)
            dataSource = new MysqlDataSource();
        
        dataSource.setURL(url);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        try {
            sqlClient = dataSource.getConnection();
            return "SUCCESS:  CONNECTED TO " + dataSource.getUrl();
        }

        catch (SQLException e) {
            e.printStackTrace();
            return "SQL ERROR:  " + e.toString();
        }

        catch (Exception e) {
            e.printStackTrace();
            return "Non-SQL ERROR: " + e.toString();
        }
    }
}
