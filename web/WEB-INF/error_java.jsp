<%-- 
    Document   : error_java
    Created on : Dec 3, 2020, 11:14:21 PM
    Author     : 839645
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="base-style.css"/>
        <link rel="stylesheet" type="text/css" href="module-style.css"/>
        <link rel="stylesheet" type="text/css" href="theme-style.css"/>
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons"
      rel="stylesheet">
        <title>JSP Page</title>
    </head>
    <body>
        <div class='error-container'> 
            <div class="content-container">
                <h1>Oops! Java threw an exception</h1>
                <p><b>Type: </b> ${pageContext.exception["class"]}</p>
                <p><b>Message: </b> ${pageContext.exception.message}</p>
            </div>
        </div>
    </body>
</html>
