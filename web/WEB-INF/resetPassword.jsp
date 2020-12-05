<%-- 
    Document   : resetPassword
    Created on : Dec 3, 2020, 11:21:40 PM
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
        <title>Reset Password</title>
    </head>
    <body>
         <div class='login-container flex flex-row'>
                <div class="login-heading-container">  
                            <div class="overlay  flex flex-column" id="login-overlay">
                                <h1 id="logo"><span id="first">HOME</span><span id="second">nVentory</span></h1>
                                <p>The one stop shop for all your inventory needs</p>
                            </div>
                </div>
                
                <div class="actual-login-container flex flex-column">
                <h3>Reset Password</h3>
                
                <c:choose>
                    <c:when test="${requestScope.reset == true}">
                        <form action="<c:url value='/reset'></c:url>" method='post'>
                            <input type="hidden" name="action" value="reset"/>
                            <label for="password">
                                New Password: 
                                <input id="password" type="text" name="newPassword" value="${password}"/>
                            </label>
                            <input type="submit" value="Reset password"/>
                        </form>
                    </c:when> 
                    <c:otherwise>
                        <form action="<c:url value='/reset'></c:url>" method='post'>
                            <input type="hidden" name="action" value="resetPassword"/>
                            <label for="username">
                                Username: 
                                <input id="username" type="text" name="resetUsername" value="${resetEmail}"/>
                            </label>
                            <input type="submit" value="Send Reset Link"/>
                        </form>
                            
                            
                    </c:otherwise>
                </c:choose>
            <c:if test="${errMsg != null}">
                <section class="errMsg"><c:out value='${requestScope.errMsg}'></c:out></section>
             </c:if>
                
            <c:if test="${infoMsg != null}">
            <section class="infoMsg"><c:out value='${infoMsg}'></c:out></section>
            </c:if>
            
            <p>Back to login <a href="<c:url value='/login'></c:url>">here</a></p>

        </div>
         </div>
        
    </body>
</html>
