import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class htmlHandler {
    public String generateHTMLTable(ResultSet sqlTable) {
        ResultSetMetaData tableMetaData;
        String htmlOut = "";
        try {
            tableMetaData = sqlTable.getMetaData();
            htmlOut = """
                        <table class = \"table bg-light\">
                            <thead class = \"bg-dark\">
                                <tr>
            """;

            for (int i = 1 ; i <= tableMetaData.getColumnCount() ; i++) {
                htmlOut += "<th class = 'bg-dark text-white' scope = \"col\">" + 
                            tableMetaData.getColumnName(i) + 
                            "</th>";
            }

            htmlOut += 
            """
                                    </tr>
                                </thead>
                                <tbody>
            """;

            while (sqlTable.next()) {
                htmlOut += "<tr>";

                for (int i = 1 ; i <= tableMetaData.getColumnCount() ; i++) {
                    if (i == 1) 
                        htmlOut += "<th class = 'bg-light' scope = \"row\">" +
                                    sqlTable.getString(i) +
                                   "</th>";
                    else 
                        htmlOut += "<td class = 'bg-light'>" +
                                    sqlTable.getString(i) +
                                   "</td>";
                }
                htmlOut += "</tr>";
            }

            htmlOut += 
                """
                                    </tr>
                                </tbody>
                            </table>
                """;
        } catch (Exception e) {
            //...
        }

        return htmlOut;
    }


    public String generateUpdateMessage(int colUpdated, int suppliersUpdated, String sqlStatement) {
        String htmlOut = """
            <div class = 'bg-success text-white' style = 'width: 600px'>
                <h4>Success:  SQL statement completed!</h4>
        """;
        htmlOut += "<p>" + colUpdated + " row(s) affected.</p> ";
        htmlOut += "<h5>Business Logic Detected! - Updating Supplier Status</h5>";
        htmlOut += "<p>Business Logic Updated " + suppliersUpdated + " status marks.</p>";
        htmlOut += "<p>SQL Statement:  " + sqlStatement + "</p>";

        htmlOut += """
            </div>
        """;
        return htmlOut;
    }

    public String generateUpdateMessage(int colUpdated, String sqlStatement) {
        String htmlOut = """
            <div class = 'bg-success text-white' style = 'width: 600px'>
                <h4>Success:  SQL statement completed!</h4>
        """;
        htmlOut += "<p>" + colUpdated + " row(s) affected.</p> ";
        htmlOut += "<p>SQL Statement:  " + sqlStatement + "</p>";
        htmlOut += """
            </div>
        """;
        return htmlOut;
    }

    public String generateErrorMessage(String errorMessage, String sqlStatement) {
        String htmlOut = """
            <div class = 'bg-danger text-white'>
                <h4>Error executing the SQL statement:</h4>
                <p>
            """;
        htmlOut += errorMessage;
        htmlOut += "<p>SQL Statement:  " + sqlStatement + "</p>";
        htmlOut += """
                </p>
            </div>
            """;

        return htmlOut;
    }
}
