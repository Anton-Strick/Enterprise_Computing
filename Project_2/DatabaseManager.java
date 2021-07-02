package Project_2;
/*
    Name: J. Anton Strickland
    Course: CNT 4714 Summer 2021
    Assignment title: Project 2 - Two-Tier Client-Server Application Development
                                  With MySQL and JDBC
    Date: June 27, 2021

    Class: DatabaseManager.java
    Description: Handles a single connection to a user defined database and 
                 the shared operationlog database. Any queries or updates
                 run through the manager by any instance to any database
                 are automatically logged.
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

    /**
     * Handles a single connection to a user defined database and 
     * the shared operationlog database.
     */
    public DatabaseManager() {
        this.operationsLog = new MysqlDataSource();
        this.clientDataSource = new MysqlDataSource();
    }

    /**
     * Attempts to establish a connection to an operations log using the
     * properties file at the provided path
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

    /**
     * Closes the manager's connection to the log. If no log is open
     * no action is taken.
     */
    private void closeLogConnection() {
        if (logConnection == null)
            return; // No active connection
        
        else {
            try {
                if (logConnection.isClosed())
                    return; // Already Closed

                logConnection.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes the client connection. If no connection is open, no
     * action is taken.
     */
    public void closeConnection() {
        if (clientConnection == null)
            return; // No active connection
        
        else {
            try {
                if (clientConnection.isClosed())
                    return; // Already Closed

                clientConnection.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Opens a connection using the parameters given
     * @param driver Name of a supported database driver
     * @param url Valid database url
     * @param username Registered username
     * @param pass Registered password
     * @throws Exception On failed connection throws relevant connection
     */
    public void openConnection(String driver, String url,
                               String username, String pass) 
                               throws Exception {
        if (clientDataSource == null) 
            clientDataSource = new MysqlDataSource();
        
        closeConnection();
        
        Class.forName(driver);
        clientDataSource.setURL(url);
        clientDataSource.setUser(username);
        clientDataSource.setPassword(pass);
        
        clientConnection = clientDataSource.getConnection();
    }

    /**
     * Increments the number of updates in the operationslog
     */
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

            closeLogConnection();
        }

        else System.out.println(this.connectToLog(propertiesPath));
    }

    /**
     * Increments the number of queries in the operationslog
     */
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
            closeLogConnection();
        }
        else System.out.println(this.connectToLog(propertiesPath));
    }

    /**
     * Execute a query, operationslog will be updated accordingly
     * @param sql String of MySQL query
     * @return A resultset matching 'sql'
     * @throws SQLException Any error arising from 'sql's execution
     */
    public ResultSet manageQuery(String sql) throws SQLException {
        ResultSet out = clientConnection.createStatement().executeQuery(sql);
        setQuery();
        return out;
    }

    /**
     * Execute an update, operationslog will be updated accordingly
     * @param sql String of MySQL command
     * @return Integer count of the number of rows affected
     * @throws SQLException Any error arising from 'sql's execution
     */
    public int manageUpdate(String sql) throws SQLException {
        int out = clientConnection.createStatement().executeUpdate(sql);
        setUpdate();
        return out;
    }
}
