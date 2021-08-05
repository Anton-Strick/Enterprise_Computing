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


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

import com.mysql.cj.jdbc.MysqlDataSource;

public class DatabaseManager {
    private MysqlDataSource clientDataSource;
    private Connection clientConnection;

    static String updateSupplierQuery = """
        UPDATE suppliers 
        SET 
            status = status + 5
        WHERE
            snum =
            """;
    static int QUANTITY_COL = 1;

    /**
     * Handles a single connection to a user defined database and 
     * the shared operationlog database.
     */
    public DatabaseManager() {
        this.clientDataSource = new MysqlDataSource();
        try {
            this.openConnection();
        } catch (Exception e) {}
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
    public void openConnection() throws Exception {
        if (clientDataSource == null) 
            clientDataSource = new MysqlDataSource();
        
        closeConnection();
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        clientDataSource.setURL("jdbc:mysql://127.0.0.1:3306/project3");
        clientDataSource.setUser("root");
        clientDataSource.setPassword("password");
        
        clientConnection = clientDataSource.getConnection();
    }

    /**
     * Execute an update, operationslog will be updated accordingly
     * @param sql String of MySQL command
     * @return Integer count of the number of rows affected
     * @throws SQLException Any error arising from 'sql's execution
     */
    public int updateQuery(String sql) throws SQLException {
        Statement statement = clientConnection.createStatement();
        return statement.executeUpdate(sql);
    }

    public ResultSet selectQuery(String sql) throws SQLException {
        Statement statement = clientConnection.createStatement();
        ResultSet sqlTable = statement.executeQuery(sql);
         
        return sqlTable;
    }

    public int checkShipmentsOver(int value) throws SQLException {
        Statement statement = clientConnection.createStatement();
        String selectCountQuery = "select COUNT(*) from shipments where quantity >= ";
        ResultSet tableOut = statement.executeQuery(selectCountQuery + value);
        tableOut.next();

        return tableOut.getInt(1);
    }

    public int updateSupplierQuery(String suppliers) throws SQLException {
        Statement statement = clientConnection.createStatement();
        String query = updateSupplierQuery + suppliers;
        return statement.executeUpdate(query);
    }
}
