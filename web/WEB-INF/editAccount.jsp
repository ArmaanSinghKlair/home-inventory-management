<%-- 
    Document   : editAccount
    Created on : Dec 2, 2020, 12:16:26 AM
    Author     : 839645
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="base-style.css"/>
        <link rel="stylesheet" type="text/css" href="module-style.css"/>
        <link rel="stylesheet" type="text/css" href="theme-style.css"/>
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons"
      rel="stylesheet">
        <title>Edit Account</title>
    </head>
    <body>
        <div class="regularUser-container">
            <header class="flex flex-row"><div class='hamburger-menu'>
                        <nav class="flex flex-column" id="hamburger-menu-nav">
                      <span class="material-icons" style="color:var(--secondary-color)" id="close-hamburger-menu">close</span>      
                    <a href="<c:url value='/inventory'></c:url>">Inventory</a>
                    <a href="<c:url value='/admin'></c:url>">Admin</a>
                </nav>
                    </div>
                    <i class="material-icons" style="color:var(--secondary-color)" id="open-hamburger-menu">menu</i>

                
                <h1 id="logo"><span id="first">HOME</span><span id="second">nVentory</span></h1>

                <nav class="flex flex-row">
                    <a href="<c:url value='/inventory'></c:url>">Inventory</a>
                    <a href="<c:url value='/admin'></c:url>">Admin</a>
                </nav>
            </header>
            <div class="regularUser-content flex flex-row">   

                    <div class="regularUser-main-content-container main-content-container">
                       <section id="inventory-items">
                           <h1>Edit Account</h1>
                           <form action="<c:url value='/edit'></c:url>" method='post'>
           
                      <input type="hidden" name="action" value="editUser"/>    
                      
                      <label for="username">
                      <input type="text" name="username" id="username" placeholder="Username" value="<c:out value='${userToEdit.username}' default=''/>" />
                     </label>
                     
                     <label for="password">
                     <input type="password" name="password" id="password" placeholder ='Password' />
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
                     <input type="submit" name="submit" value="Edit"/>
                     </label>
                     
                 </form>
                </table>
            </section>
             <c:if test="${errMsgEdit != null}">
                <section class="errMsg"><c:out value='${errMsgEdit}'></c:out></section>
             </c:if>
                
            <c:if test="${infoMsgEdit != null}">
            <section class="infoMsg"><c:out value='${infoMsgEdit}'></c:out></section>
            </c:if>
            
            <form action="<c:url value='/edit'></c:url>" method="post" id='deactivate'>
                <input type="hidden" name="action" value="deactivate" />
                <input type="submit" name="submit" value="Deactivate Account"/>
            </form>
                       
                      
                   
              </div>

                   <div class="regularUser-right-content-container right-content-container">                
                       <section class="user-info">
                           <h3><c:out value="${sessionScope.regularUser != null ? sessionScope.regularUser : sessionScope.admin}" /></h3>
                           
                           <div class='user-info-cta flex flex-row'>
                            <a href="<c:url value='/edit'></c:url>"><abbr title='Edit Account'><i class="material-icons">settings</i></abbr></a>
                            <a href="<c:url value='/login?logout'></c:url>" class="logout">Logout</a>   
                           </div>

                       </section>
                    </div>
             </div>
                      
                 
        </div>
                                                      <script src="hamburger-script.js" defer></script>

    </body>
</html>
