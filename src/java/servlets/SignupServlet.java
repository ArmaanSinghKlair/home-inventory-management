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
import services.CategoryService;

/**
 *
 * @author 839645
 */
public class SignupServlet extends HttpServlet {

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "/WEB-INF/signup.jsp";
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
        String url="/WEB-INF/signup.jsp";
        
        switch(action){
            case "register":
            String usernameToEdit = request.getParameter("username") != null ? request.getParameter("username") : "";
            String passwordToEdit = request.getParameter("password") != null ? request.getParameter("password") : "";
            String emailToEdit = request.getParameter("email") != null ? request.getParameter("email") : "";
            String firstNameToEdit = request.getParameter("firstName") != null ? request.getParameter("firstName") : "";
            String lastNameToEdit = request.getParameter("lastName") != null ? request.getParameter("lastName") : "";    
            System.out.println("WHY AM I BEIGN CALLED");
            msg = service.addNonActiveUser(usernameToEdit, passwordToEdit, emailToEdit, firstNameToEdit, lastNameToEdit);
                if(!msg.toLowerCase().startsWith("error")){
                    String status = service.sendActivateAccountMail(usernameToEdit, emailToEdit, request.getRequestURL().toString(), this.getServletContext().getRealPath("/WEB-INF"));
                    
                    if(!status.toLowerCase().startsWith("error"))
                        request.setAttribute("infoMsg", status);
                    else
                        request.setAttribute("errMsg", status);
                    
                }    
                else{
                    request.setAttribute("errMsg", msg.substring(7));
                    Users oldUser = new Users(usernameToEdit, passwordToEdit, emailToEdit, firstNameToEdit, lastNameToEdit, true, false);
                    request.setAttribute("userToEdit", oldUser);
                    
                }
            break;
        }
    

        this.getServletContext().getRequestDispatcher(url).forward(request, response);
    }
}
