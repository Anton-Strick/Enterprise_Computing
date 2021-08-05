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
    
    private DatabaseManager database;
    private htmlHandler handler;

    /**
     * Creates the required DBconnection using the init-params defined in 
     * web.xml
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.handler = new htmlHandler();
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
        session.setAttribute("sqlQuery", sqlStatement);

        sqlStatement = sqlStatement.trim();
        sqlStatement = sqlStatement.toUpperCase();

        if (sqlStatement == "") sqlStatement = "select * from suppliers";
        
        try {
            if (sqlStatement.contains("select")) {
                ResultSet sqlTable = database.selectQuery(sqlStatement);
                output = handler.generateHTMLTable(sqlTable);
            }

            else {
                int shipmentsOver100PreUpdate = database.checkShipmentsOver(100);
                int rowsUpdated = database.updateQuery(sqlStatement);
                int shipmentsOver100PostUpdate = database.checkShipmentsOver(100);

                if (shipmentsOver100PreUpdate < shipmentsOver100PostUpdate) {
                    // Retrieve Parameters for comparison
                    int startParamIndex = sqlStatement.indexOf("(");
                    int lastParamIndex = sqlStatement.indexOf(")");
                    String temp = sqlStatement.substring(startParamIndex + 1, lastParamIndex);
                    temp = temp.replaceAll("'", "");
                    temp = temp.replaceAll(" ", "");
                    String[] params = temp.split(",");
                    String supplierString = "";

                    for (String param : params) {
                        if (param.startsWith("S")) {
                            supplierString +="'" + param + "'' OR ";
                        }
                    }
                    if (supplierString.length() > 0) {
                        supplierString = supplierString.substring(0, (supplierString.length() - 4));
                        output = 
                            handler.generateUpdateMessage(rowsUpdated, database.updateSupplierQuery(supplierString), sqlStatement);
                    }
                        
                    else output = handler.generateUpdateMessage(rowsUpdated, sqlStatement);

                    
                } // End if Busness Logic Required.

                else output = handler.generateUpdateMessage(rowsUpdated, sqlStatement);
            }

        } catch (Exception e) {
            output = handler.generateErrorMessage(e.getMessage(), sqlStatement);
            e.printStackTrace();
        }
        session.setAttribute("results", output);
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

    private String updateQuery(String sql) throws SQLException {
        String output = null;
        

        int numShipments = database.updateQuery(sql);
        output += "<div> The statement executed succesfully.</div><div>" +
                        numShipments +
                   " row(s) affected</div>";

        

        
            output += "<div>Business Logic Detected! - Updating Supplier Status</div>";
            output += "<div>Business Logic Updated " + 
                          database.updateQuery(sql)  +
                      " Supplier(s) status marks</div>";

        return output;
    }
}