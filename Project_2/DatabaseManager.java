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
import java.util.Properties;

import com.mysql.cj.jdbc.MysqlDataSource;

public class DatabaseManager {
    private final String propertiesPath = "./Project_2/database.properties";

    private MysqlDataSource operationsLog;
    private Connection logConnection;

    public DatabaseManager() {
        this.operationsLog = new MysqlDataSource();
    }

    /**
     * Attempts to establish a connection to the given database using the attached
     * properties file 
     * @param path the path to the .properties file required
     */
    public String connectTo(String path) {
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

    public void setUpdate() {
        if (this.connectTo(propertiesPath).contains("SUCCESS")) {
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

        else System.out.println(this.connectTo(propertiesPath));
    }

    public void setQuery() {
        if (this.connectTo(propertiesPath).contains("SUCCESS")) {
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
        else System.out.println(this.connectTo(propertiesPath));
    }
}
