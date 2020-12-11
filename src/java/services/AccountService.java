/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.List;
import models.Users;
import dataaccess.UserDB;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author 839645
 */
public class AccountService {
    private UserDB udb = new UserDB();
    public String authenticateUser(String username, String password){
        if(isEmpty(username) || isEmpty(password))
            return "Error: All fields required";
        try { 
            return udb.authenticateUser(username, password);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: "+ex.getMessage();
        }
    }
    
    public Users getUser(String username){
        return udb.getUser(username); 
    }
     
    public List<Users> getAll(){
        return udb.getAll();
    }
    public List<Users> getAllNonAdminUsers(){
        return udb.getAllNonAdminUsers();
    }
    public List<Users> getAllAdminUsers(){
        return udb.getAllAdminUsers();
    }
    
    
    public String delete(String username){
        return udb.delete(username);
    }
    
    public String editUser(String username, String password, String email, String firstName, String lastName, String oldUsername){
        if(isEmpty(username) || isEmpty(password) || isEmpty(email) || isEmpty(firstName) || isEmpty(lastName))
            return "Error: All Fields Required";
        try {
            return udb.editUser(username, password, email, firstName, lastName, oldUsername);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: "+ex.getMessage();
        }
    }
    
     public String addUser(String username, String password, String email, String firstName, String lastName){
        if(isEmpty(username) || isEmpty(password) || isEmpty(email) || isEmpty(firstName) || isEmpty(lastName))
            return "Error: All Fields Required";
        try {
            return udb.addUser(username, password, email, firstName, lastName);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: "+ex.getMessage();

        }
    }
     
    public String addNonActiveUser(String username, String password, String email, String firstName, String lastName){
        if(isEmpty(username) || isEmpty(password) || isEmpty(email) || isEmpty(firstName) || isEmpty(lastName))
            return "Error: All Fields Required";
        try {
            return udb.addNonActiveUser(username, password, email, firstName, lastName);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: "+ex.getMessage();

        }
    }
    private boolean isEmpty(String e){
        if(e == null || e.trim().length() == 0)
            return true;
        else 
            return false;
    }
    
    public String activateUser(String username){
        if(isEmpty(username))
            return "Error: Username invalid. Please reload and try again";
        return udb.activateUser(username);
    }
    
    public String deactivateUser(String username){
        if(isEmpty(username))
            return "Error: Username invalid. Please reload and try again";
        return udb.deactivateUser(username);
    }
    
    public void addActivateAccountUuid(String username, String uuid){
        udb.addActivateAccountUuid(username, uuid);
    }
    
    public void deleteActivateAccountUuid(String username){
       udb.deleteActivateAccountUuid(username);
    }
    
    public boolean checkActivationAccountUuid(String username, String uuid){
        return udb.checkActivationAccountUuid(username, uuid);
    }
    
    public void addResetPasswordUuid(String username, String uuid){
        udb.addResetPasswordUuid(username, uuid);
    }
    
    public void deleteResetPasswordUuid(String username){
        udb.deleteResetPasswordUuid(username);  
    }
    
    public boolean checkResetPasswordUuid(String username, String uuid){
        return udb.checkResetPasswordUuid(username, uuid);
    }
    
    public String promoteUser(String username){
        if(isEmpty(username))
            return "Error: Username cannot be empty";
        else 
            return udb.promoteUser(username);  
    }
    public String demoteUser(String username){
        if(isEmpty(username))
            return "Error: Username cannot be empty";
        else 
            return udb.demoteUser(username);  
    }
    public String resetPassword(String username, String password){
        if(isEmpty(username))
            return "Error: Username cannot be empty. Please try again";
        else if(isEmpty(password))
            return "Error: Password cannot be empty";
        try {
            return udb.resetPassword(username, password);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Error: Unknown error occured";
    }
    public String sendActivateAccountMail(String toUsername, String to, String url, String path){
        HashMap<String, String> tags = new HashMap<>();
        tags.put("username", toUsername);
        String uuid = UUID.randomUUID().toString();
        udb.addActivateAccountUuid(toUsername, uuid);
        tags.put("link", url+"?uuid="+uuid+"&uname="+toUsername);
        
        String template = path+"/emailTemplates/register.html";
        String subject = "Account Activation for HOME nVentory";
        try {
            GmailService.sendMail(to, subject, template, tags);
            return "Account activation mail has been sent to your account. Please follow the instructions in the mail to complete registration";
        } catch (Exception ex) {
            Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: "+ex.getMessage();
        }
    }
    
    public String sendResetPasswordMail(String toUsername, String url, String path){
        if(this.isEmpty(toUsername))
            return "Error: Username cannot be empty";
        
        Users curUser = this.getUser(toUsername);
        if(curUser == null)
            return "Error: Invalid Username";
        
        String to = curUser.getEmail();

        HashMap<String, String> tags = new HashMap<>();
        tags.put("username", toUsername);
        String uuid = UUID.randomUUID().toString();
        udb.addResetPasswordUuid(toUsername, uuid);
        tags.put("link", url+"?uuid="+uuid+"&uname="+toUsername);
        String template = path+"/emailTemplates/resetPassword.html";
        String subject = "Reset Password - HOME nVentory";
        try {
            GmailService.sendMail(to, subject, template, tags);
            return "Reset password link has been sent to your provided email address. Please follow the instructions in the mail to complete registration";
        } catch (Exception ex) {
            Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: "+ex.getMessage();
        }
    }
    
    public void sendWelcomeEmail(String toUsername, String url, String path){
        HashMap<String, String> tags = new HashMap<>();
        String to = this.getUser(toUsername).getEmail();
        String uuid = UUID.randomUUID().toString();
        udb.addActivateAccountUuid(toUsername, uuid);        
        String template = path+"/emailTemplates/welcome.html";
        String subject = "Welcome to HOME nVentory";
        try {
            GmailService.sendMail(to, subject, template, tags);
        } catch (Exception ex) {
            Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public BufferedImage toBufferedImage(Image im)
    {
       BufferedImage bi = new BufferedImage(im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_RGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(im, 0, 0, null);
        bg.dispose();
        return bi;
    }
    
    public String updateProfilePic(String username, String image){
        return udb.updateProfilePic(username, image);
    }
    
    public String getProfilePic(String username){
        return udb.getProfilePic(username);
    }
    
    public String removeProfilePic(String username){
        return udb.removeProfilePic(username);
    }
}
