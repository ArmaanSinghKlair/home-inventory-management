/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import services.AccountService;

/**
 *
 * @author 839645
 */
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "/WEB-INF/login.jsp";
        HttpSession sess;
        if( request.getParameter("logout") != null){
            sess = request.getSession();
            String oldUsername = (sess.getAttribute("admin") != null) ? (String) sess.getAttribute("admin") :(String) sess.getAttribute("regularUser");
            sess.invalidate();
            request.setAttribute("infoMsg", "Logged out successfully");
            
            if(request.getParameter("deactivate") != null && Integer.parseInt(request.getParameter("deactivate")) == 1){
                request.setAttribute("infoMsg","Your account has been deactivated. To activate it again, please contact a system administrator");
            }
        } else if(request.getParameter("register") != null){
            request.setAttribute("infoMsg","Account registered. Continue via logging in");
        } else if(request.getParameter("auth") != null && Integer.parseInt(request.getParameter("auth")) == 0){
            request.setAttribute("errMsg","You must be logged in to access that page!");
        } else if(request.getParameter("auth") != null && Integer.parseInt(request.getParameter("auth")) == -1){
            request.setAttribute("resetPasswordLink",true);
        } else if(request.getParameter("activation") != null && Integer.parseInt(request.getParameter("activation")) == 1){
            request.setAttribute("infoMsg", "Your account has been activated. You can now login. PS: We sent you a welcome email");
        } else if(request.getParameter("reset") != null && Integer.parseInt(request.getParameter("reset")) == 1){
            request.setAttribute("infoMsg", "Your password has been reset. You can now login");
        }
        this.getServletContext().getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        AccountService ac;
        String url = "/WEB-INF/login.jsp";
        HttpSession sess;
        
        switch(action){
            case "login":
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                ac = new AccountService(); 
                String loginStatus = ac.authenticateUser(username, password);
                
                if(loginStatus.toLowerCase().startsWith("error")){
                    request.setAttribute("errMsg", loginStatus.substring(7));
                    request.setAttribute("username", username);
                if(loginStatus.substring(7).trim().toLowerCase().startsWith("password")){
                    response.sendRedirect("login?auth=-1");
                    return;
                }
                } else{
                    sess = request.getSession();
               
                    if(loginStatus.equals("admin")){
                        sess.setAttribute("admin", username);
                        response.sendRedirect("admin");
                    } else{
                        sess.setAttribute("regularUser", username);
                        response.sendRedirect("inventory");
                    }
                    return;

                }
        }
        
        this.getServletContext().getRequestDispatcher(url).forward(request, response);
    }

}
