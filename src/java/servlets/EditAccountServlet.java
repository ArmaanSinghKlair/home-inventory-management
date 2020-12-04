/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Users;
import services.AccountService;
import services.InventoryService;

/**
 *
 * @author 839645
 */
public class EditAccountServlet extends HttpServlet {

   
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sess = request.getSession();
        String username = (sess.getAttribute("admin") != null) ? (String) sess.getAttribute("admin") :(String) sess.getAttribute("regularUser");
        AccountService service = new AccountService();
        request.setAttribute("userToEdit", service.getUser(username));
        this.getServletContext().getRequestDispatcher("/WEB-INF/editAccount.jsp").forward(request, response);
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String msg;
        AccountService service = new AccountService();
        HttpSession curSess = request.getSession();
        String oldUsername = (curSess.getAttribute("admin") != null) ? (String) curSess.getAttribute("admin") :(String) curSess.getAttribute("regularUser");
        String url = "/WEB-INF/editAccount.jsp";
        switch(action){
            case "editUser":
                String usernameToEdit = request.getParameter("username") != null ? request.getParameter("username") : "";
                String passwordToEdit = request.getParameter("password") != null ? request.getParameter("password") : "";
                String emailToEdit = request.getParameter("email") != null ? request.getParameter("email") : "";
                String firstNameToEdit = request.getParameter("firstName") != null ? request.getParameter("firstName") : "";
                String lastNameToEdit = request.getParameter("lastName") != null ? request.getParameter("lastName") : "";
                msg = service.editUser( usernameToEdit, passwordToEdit, emailToEdit, firstNameToEdit, lastNameToEdit, oldUsername);
                Users oldUser;
                
                if(!msg.toLowerCase().startsWith("error")){
                    request.setAttribute("infoMsgEdit", msg); 
                    if((curSess.getAttribute("admin") != null)){
                        curSess.setAttribute("admin", usernameToEdit);
                    } else{
                        curSess.setAttribute("regularUser", usernameToEdit);
                       
                    }
                    oldUser = new Users(usernameToEdit, passwordToEdit, emailToEdit, firstNameToEdit, lastNameToEdit, service.getUser(usernameToEdit).getActive(), service.getUser(usernameToEdit).getIsAdmin());   

                }    
                else{   
                    oldUser = new Users(usernameToEdit, passwordToEdit, emailToEdit, firstNameToEdit, lastNameToEdit, service.getUser(oldUsername).getActive(), service.getUser(oldUsername).getIsAdmin());   
                    request.setAttribute("errMsgEdit", msg.substring(7));
                    
                 
                }
                request.setAttribute("userToEdit", oldUser);
                break;
                
            case "deactivate":
                msg = service.deactivateUser(oldUsername);
                
                if(!msg.toLowerCase().startsWith("error")){
                    response.sendRedirect("login?logout&deactivate=1");
                    return;
                }    
                else{   
                    request.setAttribute("errMsgEdit", msg.substring(7)); 
                }                
                break;
                
        }
                this.getServletContext().getRequestDispatcher(url).forward(request, response);

    }

   
}
