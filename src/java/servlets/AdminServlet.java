/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Categories;
import models.Users;
import services.AccountService;
import services.CategoryService;

/**
 *
 * @author 839645
 */
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "/WEB-INF/admin.jsp";
        AccountService ac = new AccountService();
        CategoryService cs = new CategoryService();
        HttpSession sess = request.getSession();
        sess.setAttribute("users", ac.getAll());
        sess.setAttribute("categories", cs.getAllCategories());
        sess.setAttribute("nonAdminUsers", ac.getAllNonAdminUsers());
        sess.setAttribute("adminUsers", ac.getAllAdminUsers());
        request.setAttribute("profilePic", ac.getProfilePic((String)sess.getAttribute("admin")));
        System.out.println(ac.getProfilePic((String)sess.getAttribute("admin")));
        this.getServletContext().getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession curSess = request.getSession();
        AccountService service = new AccountService();
        CategoryService cs = new CategoryService();
        String action = request.getParameter("action");
        String msg;
        String url="/WEB-INF/admin.jsp";
        String username;
        
        switch(action){
            case "deleteUser":
                username = request.getParameter("username");
                msg = service.delete(username);
                if(msg.toLowerCase().startsWith("error"))
                    request.setAttribute("errMsg", msg.substring(7));
                else
                    request.setAttribute("infoMsg", msg);
                break;
            case "editBtnClick":
                username = request.getParameter("username");
                request.setAttribute("userToEdit", service.getUser(username));
                request.setAttribute("editMode", true);
                curSess.setAttribute("editUsername", username);
                break;
            case "editUser":
                String usernameToEdit = request.getParameter("username") != null ? request.getParameter("username") : "";
                String passwordToEdit = request.getParameter("password") != null ? request.getParameter("password") : "";
                String emailToEdit = request.getParameter("email") != null ? request.getParameter("email") : "";
                String firstNameToEdit = request.getParameter("firstName") != null ? request.getParameter("firstName") : "";
                String lastNameToEdit = request.getParameter("lastName") != null ? request.getParameter("lastName") : "";
                msg = service.editUser( usernameToEdit, passwordToEdit, emailToEdit, firstNameToEdit, lastNameToEdit, (String) curSess.getAttribute("editUsername"));

                if(!msg.toLowerCase().startsWith("error")){
                    request.setAttribute("editMode", false);
                    request.setAttribute("infoMsgEdit", msg); 
                }    
                else{                  

                    request.setAttribute("editMode", true);
                    request.setAttribute("errMsgEdit", msg.substring(7));
                    String oldUsername = (String) curSess.getAttribute("editUsername");
                    Users oldUser = new Users(usernameToEdit, passwordToEdit, emailToEdit, firstNameToEdit, lastNameToEdit, service.getUser(oldUsername).getActive(), service.getUser(oldUsername).getIsAdmin());
                    request.setAttribute("userToEdit", oldUser);
                 
                }
                break;
            case "addUser":
                usernameToEdit = request.getParameter("username") != null ? request.getParameter("username") : "";
                passwordToEdit = request.getParameter("password") != null ? request.getParameter("password") : "";
                emailToEdit = request.getParameter("email") != null ? request.getParameter("email") : "";
                firstNameToEdit = request.getParameter("firstName") != null ? request.getParameter("firstName") : "";
                lastNameToEdit = request.getParameter("lastName") != null ? request.getParameter("lastName") : "";
                msg = service.addUser( usernameToEdit, passwordToEdit, emailToEdit, firstNameToEdit, lastNameToEdit);

                if(!msg.toLowerCase().startsWith("error")){
                    request.setAttribute("infoMsgEdit", msg); 

                }    
                else{
                    request.setAttribute("errMsgEdit", msg.substring(7));
                    Users oldUser = new Users(usernameToEdit, passwordToEdit, emailToEdit, firstNameToEdit, lastNameToEdit, true, false);
                    request.setAttribute("userToEdit", oldUser);
                    
                }
                
                break;
            case "activate":
                username = request.getParameter("username");
                msg = service.activateUser(username);
                 
                if(!msg.toLowerCase().startsWith("error")){
                    request.setAttribute("infoMsg", msg); 
                }    
                else{
                    request.setAttribute("errMsg", msg.substring(7));                    
                }
                break;
                
            case "deactivate":
                username = request.getParameter("username");
                msg = service.deactivateUser(username);
                 
                if(!msg.toLowerCase().startsWith("error")){
                    request.setAttribute("infoMsg", msg); 
                }    
                else{
                    request.setAttribute("errMsg", msg.substring(7));                    
                }
                break;
                
            case "editBtnClickCategory":
                String categoryID = request.getParameter("categoryID");
                request.setAttribute("categoryNameEdit", cs.getCategory(categoryID).getCategoryName());
                request.setAttribute("editModeCategory", true);
                curSess.setAttribute("editCategory", categoryID);
                
                break;
            case "editCategory":
                String categoryName = request.getParameter("categoryName");
                msg = cs.editCategory((String)curSess.getAttribute("editCategory"), categoryName);
                if(!msg.toLowerCase().startsWith("error")){
                    request.setAttribute("editModeCategory", false);
                    request.setAttribute("infoMsgCategory", msg); 
                }    
                else{                  

                    request.setAttribute("editModeCategory", true);
                    request.setAttribute("errMsgCategory", msg.substring(7));
                    request.setAttribute("categoryNameEdit", categoryName);
                 
                }
                break;
            case "addCategory":
                categoryName = request.getParameter("categoryName");
                msg = cs.addCategory(categoryName);
                if(!msg.toLowerCase().startsWith("error")){
                    request.setAttribute("infoMsgCategory", msg); 
                }    
                else{                  

                    request.setAttribute("errMsgCategory", msg.substring(7));
                    request.setAttribute("categoryNameEdit", categoryName);
                 
                }
                break;
                
            case "promoteUser":
                String userToPromote = request.getParameter("username");
                msg = service.promoteUser(userToPromote);
                if(msg.toLowerCase().startsWith("error")){
                    request.setAttribute("errMsgPromote", msg.substring(7));
                } else{
                    request.setAttribute("infoMsgPromote", msg);
                }
                
                break;
                
                case "demoteUser":
                String userToDemote = request.getParameter("username");
                msg = service.demoteUser(userToDemote);
                if(msg.toLowerCase().startsWith("error")){
                    request.setAttribute("errMsgPromote", msg.substring(7));
                } else{
                    request.setAttribute("infoMsgPromote", msg);
                }
                
                break;
        }
        curSess.setAttribute("users", service.getAll());
        curSess.setAttribute("categories", cs.getAllCategories());
        curSess.setAttribute("nonAdminUsers", service.getAllNonAdminUsers());
        curSess.setAttribute("adminUsers", service.getAllAdminUsers());
        request.setAttribute("profilePic", service.getProfilePic((String)curSess.getAttribute("admin")));
        this.getServletContext().getRequestDispatcher(url).forward(request, response);
        
    }

  
}
