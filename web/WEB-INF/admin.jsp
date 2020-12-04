<%-- 
    Document   : admin
    Created on : Nov 11, 2020, 12:42:41 PM
    Author     : 839645
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="base-style.css"/>
        <title>Dashboard</title>
    </head>
    <body>
  <div class="container">
            <header>
                <h1>Dashboard</h1>
                <h3><c:out value="${sessionScope.admin}" />
                <nav>
                    <a href="<c:url value='/inventory'></c:url>">Inventory</a>
                    <a href="<c:url value='/admin'></c:url>">Admin</a>
                    <a href="<c:url value='/login?logout'></c:url>">Logout</a>
                </nav>
            </header>
            
            <section id="users">
                <h3>Manage Users</h3>
                <table id="users-table">
                    <tr>
                        <th>Username</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Delete</th>
                        <th>Edit</th>
                    </tr>
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <td>${user.username}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>
                                <form action="<c:url value='/admin'></c:url>" method='post'>
                                    <input type="hidden" name="action" value="deleteUser"/>                                    
                                    <input type="hidden" name="username" value="${user.username}"/>
                                    <input type="submit" name="submit" value="Delete" />
                                </form>
                            </td>
                            <td>
                                <form action="<c:url value='/admin'></c:url>" method='post'>
                                    <input type="hidden" name="action" value="editBtnClick"/>                                    
                                    <input type="hidden" name="username" value="${user.username}"/>
                                    <input type="submit" name="submit" value="Edit" />
                                </form>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${user.active == false}">
                                        <form action="<c:url value='/admin'></c:url>" method='post'>
                                            <input type="hidden" name="action" value="activate"/>                                    
                                            <input type="hidden" name="username" value="${user.username}"/>
                                            <input type="submit" name="submit" value="Activate" />
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <form action="<c:url value='/admin'></c:url>" method='post'>
                                            <input type="hidden" name="action" value="deactivate"/>                                    
                                            <input type="hidden" name="username" value="${user.username}"/>
                                            <input type="submit" name="submit" value="Deactivate" />
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    
                </table>
            </section>
             <c:if test="${errMsg != null}">
                <section class="errMsg"><c:out value='${requestScope.errMsg}'></c:out></section>
             </c:if>
                
            <c:if test="${infoMsg != null}">
            <section class="infoMsg"><c:out value='${infoMsg}'></c:out></section>
            </c:if>
            <section id="add-users">
                <h3>${editMode ? "Edit User" : "Add User"}</h3>
                
                 <form action="<c:url value='/admin'></c:url>" method='post'>
                     <c:if test="${editMode == false || editMode == null}">
                      <input type="hidden" name="action" value="addUser"/>    
                     </c:if>
                      <c:if test="${editMode == true}">
                      <input type="hidden" name="action" value="editUser"/>    
                     </c:if>
                      
                      <label for="username">
                      <input type="text" name="username" id="username" placeholder="Username" value="<c:out value='${userToEdit.username}' default=''/>" />
                     </label>
                     
                     <label for="password">
                     <input type="password" name="password" id="password" placeholder ='Password' value="<c:out value='${userToEdit.password}' default=''/>" />
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
                     <input type="submit" name="submit" value="${editMode ? "Edit User" : "Add User"}"/>
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
            
            <section id="categories">
                <h3>Manage Categories</h3>
                <table id="categories-table">
                    <tr>
                        <th>ID</th>
                        <th>Category Name</th>
                    </tr>
                    <c:forEach var="category" items="${categories}">
                        <tr>
                            <td>${category.categoryID}</td>
                            <td>${category.categoryName}</td>
                            <td>
                                <form action="<c:url value='/admin'></c:url>" method='post'>
                                    <input type="hidden" name="action" value="editBtnClickCategory"/>                                    
                                    <input type="hidden" name="categoryID" value="${category.categoryID}"/>
                                    <input type="submit" name="submit" value="Edit" />
                                </form>
                            </td>           
                        </tr>
                    </c:forEach>
                    
                </table>
            </section>
            
            <section id="add-categories">
                <h3>${editModeCategory ? "Edit Category" : "Add Category"}</h3>
                
                 <form action="<c:url value='/admin'></c:url>" method='post'>
                     <c:if test="${editModeCategory == false || editModeCategory == null}">
                      <input type="hidden" name="action" value="addCategory"/>    
                     </c:if>
                      <c:if test="${editModeCategory == true}">
                      <input type="hidden" name="action" value="editCategory"/>    
                     </c:if>
                      
                      <label for="categoryName">
                      <input type="text" name="categoryName" id="categoryName" placeholder="Category Name" value="<c:out value='${categoryNameEdit}' default=''/>" />
                     </label>
    
                     <input type="submit" name="submit" value="${editModeCategory ? "Edit Category" : "Add Category"}"/>
                     
                 </form>
                </table>
            </section>
                     
             <c:if test="${errMsgCategory != null}">
                <section class="errMsg"><c:out value='${errMsgCategory}'></c:out></section>
             </c:if>
                
            <c:if test="${infoMsgCategory != null}">
            <section class="infoMsg"><c:out value='${infoMsgCategory}'></c:out></section>
            </c:if>
        </div>    </body>   
</html>
