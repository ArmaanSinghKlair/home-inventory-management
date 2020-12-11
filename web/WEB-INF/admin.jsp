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
        <link rel="stylesheet" type="text/css" href="module-style.css"/>
        <link rel="stylesheet" type="text/css" href="theme-style.css"/>
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons"
      rel="stylesheet">
        <title>Dashboard</title>
    </head>
    <body>
  <div class="admin-container">
            
                <header class="flex flex-row">
                    <div class='hamburger-menu'>
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
            
            
            <div class="admin-content flex flex-row">   
                <div class='admin-left-content-container left-content-container'>
                    <section class='section-links flex flex-column'>
                        <button id='users-cta' class="flex flex-row">
                            <i class="material-icons">account_box</i>Manage Users
                        </button>
                        
                        <button id='categories-cta' class="flex flex-row">
                            <i class="material-icons">category</i>Manage Categories
                        </button>
                        
                        <button id='promote-demote-cta' class="flex flex-row">
                            <i class="material-icons">group</i>Manage User Roles
                        </button>
                    </section>
                </div>
                
                <div class="admin-main-content-container main-content-container">
                <section id="users">
                <h3>Manage Users</h3>
                <table id="users-table">
                    <tr>
                        <th>Username</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Delete</th>
                        <th>Edit</th>
                        <th>Status</th>
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
                <c:if test="${errMsg != null}">
                <section class="errMsg"><c:out value='${requestScope.errMsg}'></c:out></section>
             </c:if>
                
            <c:if test="${infoMsg != null}">
            <section class="infoMsg"><c:out value='${infoMsg}'></c:out></section>
            </c:if>
            </section>
             
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
                     <input type="password" name="password" id="password" placeholder ='Password' value="" />
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
                        <th>Edit</th>
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
                <c:if test="${errMsgCategory != null}">
            <script async>window.addEventListener("load",()=>document.getElementById("categories-cta").click())</script>
                <section class="errMsg"><c:out value='${errMsgCategory}'></c:out></section>
             </c:if>
                
            <c:if test="${infoMsgCategory != null}">
            <script async>window.addEventListener("load",()=>document.getElementById("categories-cta").click())</script>
            <section class="infoMsg"><c:out value='${infoMsgCategory}'></c:out></section>
            </c:if>
            </section> 
            
                     <!-- PROMOTING/DEMOTING users -->
            <section id="promote-users">
                <h3>Promote users to administrator role</h3>
                
                 <form action="<c:url value='/admin'></c:url>" method='post'>
                      <input type="hidden" name="action" value="promoteUser"/>    
                      
                      <label for="username-to-promote">
                      <input list ="users-to-promote" name="username" id="username-to-promote" placeholder="Username" default='' autocomplete="off"/> 
                      <datalist id="users-to-promote">
                      <c:forEach items="${nonAdminUsers}" var="nonAdminUser">
                                 <option value="${nonAdminUser}">${nonAdminUser}</option>
                        </c:forEach>
                      </datalist>
                     </label>
    
                     <input type="submit" name="submit" value="Promote"/>
                     
                 </form>
                </table>
                
            </section>
                 
                 <section id="demote-users">
                <h3>Demote administrator to regular users</h3>
                
                 <form action="<c:url value='/admin'></c:url>" method='post'>
                      <input type="hidden" name="action" value="demoteUser"/>    
                      
                      <label for="username-to-demote">
                      <input list ="users-to-demote" name="username" id="username-to-demote" placeholder="Username" default='' autocomplete="off"/> 
                      <datalist id="users-to-demote">
                      <c:forEach items="${adminUsers}" var="adminUser">
                                 <option value="${adminUser}">${adminUser}</option>
                        </c:forEach>
                      </datalist>
                     </label>
    
                     <input type="submit" name="submit" value="Demote"/>
                     
                 </form>
                </table>
                
            </section>
                 <c:if test="${errMsgPromote != null}">
            <script async>window.addEventListener("load",()=>document.getElementById("promote-demote-cta").click())</script>
                <section class="errMsg"><c:out value='${errMsgPromote}'></c:out></section>
             </c:if>
                
            <c:if test="${infoMsgPromote != null}">
            <script async>window.addEventListener("load",()=>document.getElementById("promote-demote-cta").click())</script>
            <section class="infoMsg"><c:out value='${infoMsgPromote}'></c:out></section>
            </c:if>
              </div>

                   <div class="admin-right-content-container right-content-container">                
                       <section class="user-info">
                           <div class="profile-container flex flex-row">
                               <img src="<c:out value="${profilePic}"/>" alt="Profile Picture"/>
                                <h3><c:out value="${sessionScope.admin}" /></h3>
                            </div>
                           <div class='user-info-cta flex flex-row'>
                            <a href="<c:url value='/edit'></c:url>"><abbr title='Edit Account'><i class="material-icons">settings</i></abbr></a>
                            <a href="<c:url value='/login?logout'></c:url>" class="logout">Logout</a>   
                           </div>

                       </section>
                    </div>
             </div>
        </div>  
                           <script  type="text/javascript">
                                function downloadJSAtOnload() {
                                let scripts = ['adminHandle.js','hamburger-script.js'];
                                <c:if test="${editModeCategory == true}">
                                scripts.push("editModeScript.js");
                                </c:if>
                                <c:if test="${infoMsgPromote != null || errMsgPromote != null}">
                                scripts.push("promoteScript.js");
                                </c:if>
                                <c:if test="${infoMsgCategory != null || errMsgCategory != null}">
                                scripts.push("categoryScript.js");
                                </c:if>
                                let element;
                                scripts.forEach((e)=>{
                                    element = document.createElement("script");
                                    element.src = e;
                                    element.async = true;
                                    document.body.appendChild(element);
                                });
                                
                                }
                                if (window.addEventListener)
                                window.addEventListener("load", downloadJSAtOnload, false);
                                else if (window.attachEvent)
                                window.attachEvent("onload", downloadJSAtOnload);
                                else window.onload = downloadJSAtOnload;
                             </script>
                           
                       
                            
                            <c:if test="${infoMsgCategory != null || errMsgCategory != null}">
                            <script async>window.addEventListener("load",()=>document.getElementById("categories-cta").click())</script>
                           </c:if>

    </body>   
</html>
