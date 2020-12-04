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
public class ResetServlet extends HttpServlet {
@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "/WEB-INF/resetPassword.jsp";
        this.getServletContext().getRequestDispatcher(url).forward(request, response);
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession curSess = request.getSession();
        AccountService service = new AccountService();
        String action = request.getParameter("action");
        String msg;
        String url="/WEB-INF/resetPassword.jsp";
        
        switch(action){
            case "resetPassword":
            String resetUsername = request.getParameter("resetUsername") != null ? request.getParameter("resetUsername") : "";
            
                    String status = service.sendResetPasswordMail(resetUsername, request.getRequestURL().toString(), this.getServletContext().getRealPath("/WEB-INF"));

                    if(!status.toLowerCase().startsWith("error"))
                        request.setAttribute("infoMsg", status);
                    else{
                        request.setAttribute("errMsg", status);
                        request.setAttribute("resetEmail", resetUsername);

                    }
               
            break;
            case "reset":
                String newPassword = request.getParameter("newPassword") != null ? request.getParameter("newPassword") : "";
                String usernamePasswordReset = (String) curSess.getAttribute("usernamePasswordReset");
                msg = service.resetPassword(usernamePasswordReset, newPassword);
                
                if(msg.toLowerCase().startsWith("error")){
                    request.setAttribute("errMsg", msg);
                    request.setAttribute("resetPassword", newPassword);
                    request.setAttribute("reset", true);

                } else{
                    curSess.setAttribute("usernamePasswordReset", null);
                    response.sendRedirect("login?reset=1");
                    return;
                }
        }
    

        this.getServletContext().getRequestDispatcher(url).forward(request, response);
    }
}
