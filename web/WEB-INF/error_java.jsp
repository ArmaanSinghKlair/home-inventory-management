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
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Oops! Java threw an exception</h1>
        <div class='container'>
            <h3>Details</h3>
            <p><b>Type: </b> ${pageContext.exception["class"]}</p>
            <p><b>Message: </b> ${pageContext.exception.message}</p>
        </div>
    </body>
</html>
