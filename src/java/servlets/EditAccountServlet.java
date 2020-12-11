/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Users;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.util.Streams;
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
        request.setAttribute("profilePic", service.getProfilePic(username));
        this.getServletContext().getRequestDispatcher("/WEB-INF/editAccount.jsp").forward(request, response);
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String url = "/WEB-INF/editAccount.jsp";
        String msg;
        AccountService service = new AccountService();
        HttpSession curSess = request.getSession();
        String oldUsername = (curSess.getAttribute("admin") != null) ? (String) curSess.getAttribute("admin") :(String) curSess.getAttribute("regularUser");
        if(ServletFileUpload.isMultipartContent(request)){
            try {
                ServletFileUpload upload = new ServletFileUpload();
                FileItemIterator iter = upload.getItemIterator(request);
                while(iter.hasNext()){
                    FileItemStream fis = iter.next();
                    String name = fis.getFieldName();
                    InputStream is = fis.openStream();
                    
                    if(fis.isFormField()){
                        action = Streams.asString(is);
                        if(!action.equals("editProfile") && name.equals("action")){
                            request.setAttribute("errMsgEdit", "Error Occured. Please try again");
                            break;
                        }
                    } else{

                        String fileType = fis.getContentType();
                        if(Pattern.matches("[iI][mM][aA][gG][eE]/\\w*", fileType)){

                            // Scales image from InputStream, Converts to byte[] and encodes it
                            Image m = ImageIO.read(is);
                            m = m.getScaledInstance(75, 75, Image.SCALE_SMOOTH);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            ImageIO.write(service.toBufferedImage(m), fileType.substring(6), bos);
                            Encoder e = Base64.getMimeEncoder();
                            String encodedImage = e.encodeToString(bos.toByteArray());
                            service.updateProfilePic(oldUsername, "data:image/"+fileType.substring(6)+";base64,"+encodedImage);
                            request.setAttribute("infoMsgEdit", "Profile pic updated successfully");
                        }
                    }
                    
                }
            } catch (FileUploadException ex) {
                Logger.getLogger(EditAccountServlet.class.getName()).log(Level.SEVERE, null, ex);
                request.setAttribute("errMsgEdit", "Internal Error occured. Please try again");
            }
                    
        } else {
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
                    
                case "removeProfile":
                    msg = service.removeProfilePic(oldUsername);
                    if(!msg.toLowerCase().startsWith("error")){
                        request.setAttribute("infoMsgEdit", msg);
                    }    
                    else{   
                        request.setAttribute("errMsgEdit", msg.substring(7)); 
                    }  
                    break;

            }
           }
                this.getServletContext().getRequestDispatcher(url).forward(request, response);

    }

   
}
