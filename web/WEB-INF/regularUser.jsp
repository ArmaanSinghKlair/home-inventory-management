<%-- 
    Document   : inventory
    Created on : Nov 11, 2020, 12:42:35 PM
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

        <title>Inventory</title>
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
                           <h1>Your items</h1>
                           
                       <table id="inventory-items-table">
                           <c:choose>
                           <c:when test="${!(items == null || items.size() == 0)}">
                           <tr>
                               <th>Category</th>
                               <th>Name</th>
                               <th>Price</th>
                               <th>Delete</th>
                               <th>Edit</th>
                           </tr>

                               <c:forEach var="item" items="${items}">
                                   <tr>
                                       <td>${item.category.categoryName}</td>
                                       <td>${item.itemName}</td>
                                       <td>
                                           ${item.price}
                                       </td>
                                       <td>
                                           <form action="<c:url value='/inventory'></c:url>" method='post'>
                                               <input type="hidden" name="action" value="deleteItem"/>                                    
                                               <input type="hidden" name="itemId" value="${item.itemID}"/>
                                               <input type="submit" name="submit" value="Delete" />
                                           </form>
                                       </td>
                                       <td>
                                           <form action="<c:url value='/inventory'></c:url>" method='post'>
                                               <input type="hidden" name="action" value="editItemBtnClick"/>                                    
                                               <input type="hidden" name="itemId" value="${item.itemID}"/>
                                               <input type="submit" name="submit" value="Edit" />
                                           </form>
                                       </td>
                                   </tr>
                               </c:forEach>
                           </c:when>
                               <c:otherwise>
                                   <tr>
                                       <th> No Items in inventory!</th>
                                   </tr>
                               </c:otherwise>
                           </c:choose>
                       </table>
                   </section>
                       <c:if test="${errMsg != null}">
                       <section class="errMsg"><c:out value='${requestScope.errMsg}'></c:out></section>
                    </c:if>

                   <c:if test="${infoMsg != null}">
                   <section class="infoMsg"><c:out value='${infoMsg}'></c:out></section>
                   </c:if>
                   
                   <section id="add-items">
                       <h3>${requestScope.editModeItem ? "Edit Item" : "Add Item"}</h3>

                        <form action="<c:url value='/inventory'></c:url>" method='post'>
                             <input type="hidden" name="action" value="${requestScope.editModeItem ? "editItem" : "addItem"}"/>    

                             <c:if test="${editModeItem = true}">
                                 <input type="hidden" name="itemID" value="${itemID}"/>
                             </c:if>
                             <label for="categoryName">
                                 <select id="categoryName" name="categoryId" value="${categoryId}">
                                 <c:forEach var="category" items="${categories}">
                                     <option value="${category.categoryID}">${category.categoryName}</option>
                                 </c:forEach>
                                </select>
                            </label>

                            <label for="itemName">
                            <input type="text" name="itemName" placeholder ='Item Name' value="<c:out value='${itemName}' default=''/>" />
                            </label>

                            <label for="itemPrice">
                            <input type="number" step="0.01" name="itemPrice" placeholder ='Item Price' value="<c:out value='${itemPrice}' default=''/>" />
                            </label>

                            <input type="submit" name="submit" value="${requestScope.editModeItem ? "Edit Item" : "Add Item"}"/>

                        </form>
                       </table>
                   </section>
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
                           <script src="hamburger-script.js"></script>
    </body>
</html>
