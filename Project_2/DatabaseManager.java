package Project_2;
/*
    Name: J. Anton Strickland
    Course: CNT 4714 Summer 2021
    Assignment title: Project 2 - Two-Tier Client-Server Application Development With
                                  MySQL and JDBC
    Date: June 27, 2021

    Class: DatabaseManager.java
    Description: Contains the elementIDs and functions used by the javaFX UI
                 Designed using Scene Builder. Is used by sqlApp.java to report
                 user inputs, and receives information via string from sqlApp
                 to display in UI windows.
*/

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.cj.jdbc.MysqlDataSource;

public class DatabaseManager {
    private final String propertiesPath = "./Project_2/database.properties";

    private MysqlDataSource operationsLog;
    private Connection logConnection;

    private MysqlDataSource clientDataSource;
    private Connection clientConnection;

    public DatabaseManager() {
        this.operationsLog = new MysqlDataSource();
        this.clientDataSource = new MysqlDataSource();
    }

    /**
     * Attempts to establish a connection to the given database using the attached
     * properties file 
     * @param path the path to the .properties file required
     */
    private String connectToLog(String path) {
        Properties properties = new Properties();
        FileInputStream propertiesFile;
        try {
            //--------------------- Load Properties File --------------------//
            propertiesFile = new FileInputStream(path);
            properties.load(propertiesFile);
            propertiesFile.close();

            //--------------------- Configure DataSource --------------------//
            operationsLog.setURL(properties.getProperty("MYSQL_DB_URL"));
            operationsLog.setUser(properties.getProperty("MYSQL_USERNAME"));
            operationsLog.setPassword(properties.getProperty("MYSQL_PASSWORD"));

            //-------------------- Connect to DataSource --------------------//
            logConnection = operationsLog.getConnection();
            return "SUCCESS: CONNECTED TO " + operationsLog.getUrl();
        }

        catch (Exception e) {
            return "ERROR:  " + e.toString();
        }
    }

    public void closeConnection() {
        if (logConnection == null)
            return; // No active connection
        
        else {
            try {
                if (logConnection.isClosed())
                    return; // Already Closed

                logConnection.close();
            }
            catch (Exception e) {}
        }
    }

    public void openConnection(String driver, String url,
                               String username, String pass) 
                               throws Exception {
        if (clientDataSource == null) 
            clientDataSource = new MysqlDataSource();
        
        if (clientConnection != null) {
            if (!clientConnection.isClosed()) {
                clientConnection.close();
            }
        }
        
        Class.forName(driver);
        clientDataSource.setURL(url);
        clientDataSource.setUser(username);
        clientDataSource.setPassword(pass);
        
        clientConnection = clientDataSource.getConnection();
    }

    private void setUpdate() {
        if (this.connectToLog(propertiesPath).contains("SUCCESS")) {
            try {
                logConnection.createStatement().executeUpdate(
                    "UPDATE operationscount " +
                    "SET num_updates = num_updates + 1;"
                );
            }

            catch (Exception e) {
                e.printStackTrace();
            }

            closeConnection();
        }

        else System.out.println(this.connectToLog(propertiesPath));
    }

    private void setQuery() {
        if (this.connectToLog(propertiesPath).contains("SUCCESS")) {
            try {
                logConnection.createStatement().executeUpdate(
                    "UPDATE operationscount " +
                    "SET num_queries = num_queries + 1;"
                );
            }

            catch (Exception e) {
                e.printStackTrace();
            }
            closeConnection();
        }
        else System.out.println(this.connectToLog(propertiesPath));
    }

    public ResultSet manageQuery(String sql) throws SQLException {
        ResultSet out = clientConnection.createStatement().executeQuery(sql);
        setQuery();
        return out;
    }

    public int manageUpdate(String sql) throws SQLException {
        int out = clientConnection.createStatement().executeUpdate(sql);
        setUpdate();
        return out;
    }
}
