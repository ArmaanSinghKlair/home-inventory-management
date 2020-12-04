/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.List;
import models.Users;
import dataaccess.UserDB;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author 839645
 */
public class AccountService {
    public String authenticateUser(String username, String password){
        if(isEmpty(username) || isEmpty(password))
            return "Error: All fields required";
        return new UserDB().authenticateUser(username, password); 
    }
    
    public Users getUser(String username){
        return new UserDB().getUser(username); 
    }
     
    public List<Users> getAll(){
        return new UserDB().getAll();
    }
    
    public String delete(String username){
        return new UserDB().delete(username);
    }
    
    public String editUser(String username, String password, String email, String firstName, String lastName, String oldUsername){
        if(isEmpty(username) || isEmpty(password) || isEmpty(email) || isEmpty(firstName) || isEmpty(lastName))
            return "Error: All Fields Required";
        return new UserDB().editUser(username, password, email, firstName, lastName, oldUsername);
    }
    
     public String addUser(String username, String password, String email, String firstName, String lastName){
        if(isEmpty(username) || isEmpty(password) || isEmpty(email) || isEmpty(firstName) || isEmpty(lastName))
            return "Error: All Fields Required";
        return new UserDB().addUser(username, password, email, firstName, lastName);
    }
     
    public String addNonActiveUser(String username, String password, String email, String firstName, String lastName){
        if(isEmpty(username) || isEmpty(password) || isEmpty(email) || isEmpty(firstName) || isEmpty(lastName))
            return "Error: All Fields Required";
        return new UserDB().addNonActiveUser(username, password, email, firstName, lastName);
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
        return new UserDB().activateUser(username);
    }
    
    public String deactivateUser(String username){
        if(isEmpty(username))
            return "Error: Username invalid. Please reload and try again";
        return new UserDB().deactivateUser(username);
    }
    
    public void addActivateAccountUuid(String username, String uuid){
        new UserDB().addActivateAccountUuid(username, uuid);
    }
    
    public void deleteActivateAccountUuid(String username){
        new UserDB().deleteActivateAccountUuid(username);
    }
    
    public boolean checkActivationAccountUuid(String username, String uuid){
        return new UserDB().checkActivationAccountUuid(username, uuid);
    }
    
    public void addResetPasswordUuid(String username, String uuid){
        new UserDB().addActivateAccountUuid(username, uuid);
    }
    
    public void deleteResetPasswordUuid(String username){
        new UserDB().deleteActivateAccountUuid(username);
    }
    
    public boolean checkResetPasswordUuid(String username, String uuid){
        return new UserDB().checkResetPasswordUuid(username, uuid);
    }
    
    public String resetPassword(String username, String password){
        if(isEmpty(username))
            return "Error: Username cannot be empty. Please try again";
        else if(isEmpty(password))
            return "Error: Password cannot be empty";
        return new UserDB().resetPassword(username, password);
    }
    public String sendActivateAccountMail(String toUsername, String to, String url, String path){
        HashMap<String, String> tags = new HashMap<>();
        tags.put("username", toUsername);
        String uuid = UUID.randomUUID().toString();
        new UserDB().addActivateAccountUuid(toUsername, uuid);
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
        new UserDB().addResetPasswordUuid(toUsername, uuid);
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
        new UserDB().addActivateAccountUuid(toUsername, uuid);        
        String template = path+"/emailTemplates/welcome.html";
        String subject = "Welcome to HOME nVentory";
        try {
            GmailService.sendMail(to, subject, template, tags);
        } catch (Exception ex) {
            Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
