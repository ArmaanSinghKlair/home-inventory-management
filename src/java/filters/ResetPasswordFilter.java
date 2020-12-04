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
public class ResetPasswordFilter implements Filter {
    
    private FilterConfig filterConfig = null;
    

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            AccountService ac = new AccountService();
            String uuid = httpRequest.getParameter("uuid");
            String username  = httpRequest.getParameter("uname");
            HttpSession sess = httpRequest.getSession();
            
            if(uuid != null && username != null){
                if(!ac.checkResetPasswordUuid(username, uuid)){
                    httpRequest.setAttribute("errMsg", "Invalid link for reset. Please follow instructions in your email to reset");
                } else{
                    ac.deleteResetPasswordUuid(username);
                    httpRequest.setAttribute("reset", true);
                    sess.setAttribute("usernamePasswordReset", username);
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
