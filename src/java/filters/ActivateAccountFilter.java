/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import services.AccountService;

/**
 *
 * @author 839645
 */
public class ActivateAccountFilter implements Filter {
    
    private FilterConfig filterConfig = null;
    

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            AccountService ac = new AccountService();
            String uuid = httpRequest.getParameter("uuid");
            String username  = httpRequest.getParameter("uname");
            if(uuid != null && username != null){
                if(!ac.checkActivationAccountUuid(username, uuid)){
                    httpRequest.setAttribute("errMsg", "Invalid link for activation or account already activated. Please follow instructions in your email to activate");
                } else{
                    ac.activateUser(username);
                    ac.deleteActivateAccountUuid(username);
                    ac.sendWelcomeEmail(username,httpRequest.getRequestURL().toString(), filterConfig.getServletContext().getRealPath("/WEB-INF"));
                    httpResponse.sendRedirect("login?activation=1");
                    return;
                }
            }
                
            chain.doFilter(request, response);
       
    }

    public void destroy() {        
    }

    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
    }
}
