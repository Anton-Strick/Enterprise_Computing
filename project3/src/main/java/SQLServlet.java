/*
    Name: J. Anton Strickland
    Course: CNT 4714 Summer 2021
    Assignment title: Project 3 - Developing A Three-Tier Distributed Web-Based 
                                  Application
    Date: August 1, 2021

    Class: SQLServlet
    Description: A JSP which allows clients to enter SQL commands into a browser
                 based window and recieve any outputs (including errors)
*/

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/SQLServlet")
public class SQLServlet extends HttpServlet {
    
    private Statement statement;
    private DatabaseManager database;

    /**
     * Creates the required DBconnection using the init-params defined in 
     * web.xml
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            this.database = new DatabaseManager();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new UnavailableException(e.getMessage());
        }
        
    }


    /**
     * Executes a HTTP Get operation
     * 
     * @param request the request recieved from the front-end web application
     * @param response the response to be processed by the front-end
     * @throws ServletException If there is a problem in JPS
     * @throws IOException If there is an error with the strings
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                         throws ServletException, IOException {
        String output = "";
        HttpSession session = request.getSession();
        String sqlStatement = request.getParameter("sqlQuery");
        session.setAttribute("results", output);
        session.setAttribute("sqlQuery", sqlStatement);

        sqlStatement.trim().toLowerCase();
        
        try {
            database.manageQuery(sqlStatement);
        } catch (Exception e) {
            output = "<span>" + e.getMessage() + "</span>";
            e.printStackTrace();
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Executes a HTTP Post operation
     * 
    * @param request the request recieved from the front-end web application
     * @param response the response to be processed by the front-end
     * @throws ServletException If there is a problem in JPS
     * @throws IOException If there is an error with the strings
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
                          throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Runs the appropriate query based on the contents of the string
     * 
     * @param query The query in the form of a string
     * @return The output of the MySQL Server
     * @throws SQLException Any error that may be thrown by the server
     */
    private String runQuery(String query) throws SQLException {
        if (query.contains("select")) {
            return selectQuery(query);
        }

        else return updateQuery(query);
    }

    /**
     * Runs a selection query on the attached database
     * 
     * @param query The query in the form of a string
     * @return The output of the MySQL Server
     * @throws SQLException Any error that may be thrown by the server
     */
    private String selectQuery(String query) throws SQLException {
        ResultSet sqlTable = statement.executeQuery(query);
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

    private String updateQuery(String sql) throws SQLException {
        String output = null;
        int shipmentsOver100PreUpdate = checkShipmentsOver(100);

        int numShipments = statement.executeUpdate(sql);
        output += "<div> The statement executed succesfully.</div><div>" +
                        numShipments +
                   " row(s) affected</div>";

        int shipmentsOver100PostUpdate = checkShipmentsOver(100);

        if (shipmentsOver100PreUpdate < shipmentsOver100PostUpdate) {
            output += "<div>Business Logic Detected! - Updating Supplier Status</div>";
            output += "<div>Business Logic Updated " + 
                          statement.executeUpdate(sql) + 
                      " Supplier(s) status marks</div>";
        }

        return output;
    }

    private int checkShipmentsOver(int value) throws SQLException {
        String selectCountQuery = "select COUNT(*) from shipments where quantity >= ";
        ResultSet tableOut = statement.executeQuery(selectCountQuery + value);
        tableOut.next();

        return tableOut.getInt(1);
    }
}