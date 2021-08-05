<%-- 
    Name: J. Anton Strickland
    Course: CNT 4714 Summer 2021
    Assignment title: Project 3 - Developing A Three-Tier Distributed Web-Based 
                                  Application
    Date: August 1, 2021

    Class: SQLServlet
    Description: A JSP which allows clients to enter SQL commands into a browser
                 based window and recieve any outputs (including errors)
--%>

<%@ page import="java.util.Vector" import = "java.io.PrintWriter" language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="https://fonts.googleapis.com/css?family=Merriweather+Sans:400,700" rel="stylesheet" />
    <link href="https://fonts.googleapis.com/css?family=Merriweather:400,300,300italic,400italic,700,700italic" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="styles.css">
        <style>
            .navbar {
                margin-bottom: 0;
                border-radius: 0;
            }
        </style>
    <title>Project 3</title>
    <script src="reset.js"></script>
</head>

<body id = "page">
    <section class = "page-section">
        <div class = "row align-items-center justify-content-center text-center">
            <div class = "col-lg-8 align-self-end">
                <h1 class="text-center col-sm-12 col-md-12 col-lg-12">Project 3</h1>
                <hr class = "divider" />
                <p class = "text-center">
                    Please enter any valid SQL query or update statement in the field below.
                </p>
                <form method = "post" action = "SQLServlet">
                    <textarea id = "sqlQuery" name = "sqlQuery" rows="8" cols="50" style="height: 300px; width: 627px; margin-left: 0px; margin-right: 0px; ">
                        Enter a Statement
                    </textarea>
                    <hr>
                    <div>
                        <input class = "btn btn-primary" type = "reset" value = "Clear" name = "Clear">
                        <input class = "btn btn-primary" type = "submit" value = "Execute Command" name = "Execute">
                    </div>
                    
                </form>
            </div>
        </div>
    </section>

    <section class = "page-section bg-primary" style="text-align: -webkit-center;">
        <div class = "row-align-items-center justify-content-center text-center">
            <h1 class="text-white mt-0">Execution Results</h1>
        </div>
        <hr class="divider-light" />
        <div class = "col-lg-8">
            <%
            String results = "";
            results = (String)session.getAttribute("results");
            PrintWriter mOut = response.getWriter();
            if (results == null) {
                results = "";
            }
            %>
        <%= results %>
        </div>
    </section>

    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
        crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js" integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T"
        crossorigin="anonymous"></script>
</body>

</html>