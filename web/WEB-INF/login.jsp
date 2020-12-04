<%-- 
    Document   : login
    Created on : Nov 11, 2020, 12:42:28 PM
    Author     : 839645
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="base-style.css"/>
        <link rel="stylesheet" type="text/css" href="theme-style.css"/>
        <link rel="stylesheet" type="text/css" href="module-style.css"/>

        <title>Login</title>
    </head>
    <body>
                 <div class="login-container flex flex-row">
                        <div class="login-heading-container">  
                            <div class="overlay  flex flex-column" id="login-overlay">
                                <h1 id="logo"><span id="first">HOME</span><span id="second">nVentory</span></h1>
                                <p>The one stop shop for all your inventory needs</p>
                            </div>
                        </div>
                     
                        <div class="actual-login-container flex flex-column">
                            <h3>Login</h3>
                            <form action="<c:url value='/login'></c:url>" method="post">
                                <input type="hidden" name="action" value="login"/>
                                <label for="username">
                                    <input type="text" id="username" name="username" value="<c:out value='${requestScope.username}' default=''/>" placeholder="Username" />
                                </label>

                                <label for="password">
                                    <input type="password" id="username" name="password" placeholder="Password"/>
                                </label>

                                <input type="submit" name="submit" value="Login"/>
                            </form>

                        <c:if test="${errMsg != null}">
                            <section class="errMsg"><c:out value='${errMsg}'></c:out></section>
                         </c:if>

                        <c:if test="${infoMsg != null}">
                        <section class="infoMsg"><c:out value='${infoMsg}'></c:out></section>
                        </c:if>
                        <c:if test="${resetPasswordLink == true}">
                        Forgot Password? <a href="<c:url value='/reset'></c:url>">Reset</a>
                        </c:if>
                        <p>Don't have an account? <a href="<c:url value='/signup'></c:url>">Register here</a></p>
                     </div>

                 </div>
    </body>
</html>
