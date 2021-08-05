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
            snum IN (SELECT DISTINCT
                    snum
                FROM
                    shipments
                        LEFT JOIN
                    shipmentsBeforeUpdate USING (snum , pnum , jnum , quantity)
                WHERE
                    shipmentsBeforeUpdate.snum IS NULL)
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
     * Execute a query, operationslog will be updated accordingly
     * @param sql String of MySQL query
     * @return A resultset matching 'sql'
     * @throws SQLException Any error arising from 'sql's execution
     */
    public String manageQuery(String sql) throws SQLException {
        if (sql.contains("select"))
            return selectQuery(sql);

        return updateQuery(sql);
    }

    /**
     * Execute an update, operationslog will be updated accordingly
     * @param sql String of MySQL command
     * @return Integer count of the number of rows affected
     * @throws SQLException Any error arising from 'sql's execution
     */
    private String updateQuery(String sql) throws SQLException {
        return "Ran Update Query";
    }

    private String selectQuery(String sql) throws SQLException {
        Statement statement = clientConnection.createStatement();
        ResultSet sqlTable = statement.executeQuery(sql);
        ResultSetMetaData tableMetaData = sqlTable.getMetaData();

        String output = 
            """
            <div class = \"container-fluid\">
                <div class = \"row justify-content-center\">
                    <div class = \"table-responsive-sm-10 table-responsive-md-10 table-responsive-lg-10\">
                        <table class = \"table\">
                            <thead class = \"thead-dark\">
                                <tr>
            """;
        
        for (int i = 1 ; i <= tableMetaData.getColumnCount() ; i++) {
            output += "<th scope = \"col\">" + 
                          tableMetaData.getColumnName(i) + 
                      "</th>";
        }

        output += 
        """
                                </tr>
                            </thead>
                            <tbody>
        """;

        while (sqlTable.next()) {
            output += "<tr>";

            for (int i = 1 ; i <= tableMetaData.getColumnCount() ; i++) {
                if (i == 1) 
                    output += "<th scope = \"row\">" +
                                   sqlTable.getString(i) +
                              "</th>";
                else 
                    output += "<td>" +
                                   sqlTable.getString(i) +
                              "</td>";
            }
            output += "</tr>";
        }

        output += 
            """
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            """;
        return output;
    }
}
