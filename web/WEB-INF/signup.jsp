<%-- 
    Document   : signup
    Created on : Dec 2, 2020, 10:25:00 PM
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
        <title>Register Account</title>
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
                <h3>Register Account</h3>
                
                 <form action="<c:url value='/signup'></c:url>" method='post'>
                    
                      <input type="hidden" name="action" value="register"/>    
                      
                      <label for="username">
                      <input type="text" name="username" id="username" placeholder="Username" value="<c:out value='${userToEdit.username}' default=''/>" />
                     </label>
                     
                     <label for="password">
                     <input type="password" name="password" id="password" placeholder ='Password'  />
                     </label>
                     
                     <label for="email">
                     <input type="email" name="email" id="email" placeholder="Email" value="<c:out value='${userToEdit.email}' default=''/>" />
                     </label>
                     
                     <label for="firstName">
                     <input type="text" name="firstName" id="firstName" placeholder="First Name" value="<c:out value='${userToEdit.firstName}' default=''/>" />
                     </label>
                     
                     <label for="lastName">
                     <input type="text" name="lastName" id="lastName" placeholder="Last Name" value="<c:out value='${userToEdit.lastName}' default=''/>" />
                     </label>
                     
                     <label for="username">
                     <input type="submit" name="submit" value="Register"/>
                     </label>
                     
                 </form>
                </table>
                     
            <c:if test="${requestScope.errMsg != null}">
                <section class="errMsg"><c:out value='${requestScope.errMsg}'></c:out></section>
            </c:if>
                
            <c:if test="${requestScope.infoMsg != null}">
            <section class="infoMsg"><c:out value='${requestScope.infoMsg}'></c:out></section>
            </c:if>
             <p>Already have an account? <a href="<c:url value='/login'></c:url>">Login here</a></p>
                </div>
            </div>
    </body>
</html>
