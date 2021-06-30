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

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.cj.jdbc.MysqlDataSource;

public class DatabaseManager {
    private MysqlDataSource database;
    private Connection connection;

    public DatabaseManager(MysqlDataSource db, Connection c) {
        this.setDatabase(db);
        this.setConnection(c);
    }

    public DatabaseManager() {
        // Generic Constructor
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
            database.setURL(properties.getProperty("MYSQL_DB_URL"));
            database.setUser(properties.getProperty("MYSQL_USERNAME"));
            database.setPassword(properties.getProperty("MYSQL_PASSWORD"));

            //-------------------- Connect to DataSource --------------------//
            this.connection = database.getConnection();
            return "SUCCESS: CONNECTED TO " + database.getUrl();
        }

        catch (Exception e) {
            return "ERROR:  " + e.toString();
        }
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
        this.database.setURL(url);
        database.setUser(user);
        database.setPassword(password);

        try {
            this.connection = database.getConnection();
            return "SUCCESS:  CONNECTED TO " + database.getUrl();
        }

        catch (SQLException e) {
            e.printStackTrace();
            return "ERROR:  " + e.toString();
        }

        catch (Exception e) {
            e.printStackTrace();
            return "ERROR: " + e.toString();
        }
    }

    /**
     * Sets the passed database as this manager's database
     * @param db any MysqlDataSource, will be initialized if null
     */
    public void setDatabase(MysqlDataSource db) {
        if (db == null)
            db = new MysqlDataSource();
        
        this.database = db;
    }

    public void setConnection(Connection c) {
        if (this.connection != null)
            this.closeConnection();
        this.connection = c;
    }

    public void closeConnection() {
        if (this.connection == null)
            return; // No active connection

        else {

        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public MysqlDataSource getDatabase() {
        return this.database;
    }
}
