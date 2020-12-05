/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.persistence.*;
import javax.persistence.EntityManagerFactory;
import models.*;
import utilities.DBUtil;
import utilities.PasswordUtil;
/**
 *
 * @author 839645
 */

public class UserDB {
    public String authenticateUser(String username, String password) throws NoSuchAlgorithmException{
         EntityManagerFactory emf = DBUtil.getEmFactory();
        EntityManager em = emf.createEntityManager();
        
        try{
            Users user = em.find(Users.class, username);
            String salt = user.getPasswordSalt() != null ? user.getPasswordSalt() : "";
            if(user == null){
                return "Error: Username is invalid";
            } else if(!user.getPassword().trim().equals(PasswordUtil.hashPassword(password+salt))){
                return "Error: Password is invalid";

            } else if(!user.getActive()){
                return "Error: Your account has been deactivated. Please talk to a system admin to reactivate";
            } else {
                if(user.getIsAdmin())
                    return "admin";
                else
                    return "regularUser";
            } 
            
        } finally{
            em.close();
        }
    }
    
    public Users getUser(String username){
         EntityManagerFactory emf = DBUtil.getEmFactory();
        EntityManager em = emf.createEntityManager();
        
        try{
            Users user = em.find(Users.class, username);
            return user;
            
        } finally{
            em.close();
        }
    }
   
    public List<Users> getAll(){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        
        try{
            TypedQuery<Users> q = em.createNamedQuery("Users.findAll", Users.class);
            List<Users> users = q.getResultList();
            if(users == null || users.isEmpty())
                users = null;
            return users;
        } finally{
            em.close();
        }
    }
    
    public List<Users> getAllNonAdminUsers(){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        
        try{
            TypedQuery<Users> q = em.createQuery("select u.username from Users u where u.isAdmin = false", Users.class);
            List<Users> users = q.getResultList();
            if(users == null || users.isEmpty())
                users = null;
            return users;
        } finally{
            em.close();
        }
    }
    
    public List<Users> getAllAdminUsers(){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        
        try{
            TypedQuery<Users> q = em.createQuery("select u.username from Users u where u.isAdmin = true", Users.class);
            List<Users> users = q.getResultList();
            if(users == null || users.isEmpty())
                users = null;
            return users;
        } finally{
            em.close();
        }
    }
    
    public String delete(String username){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try{
           Users user = em.find(Users.class, username);
           if(user.getIsAdmin())
               return "Error: Cannot delete an administrator";
           
           trans.begin();
           em.remove(user);
           trans.commit();
           
           return "User removed successfully";
        } catch(Exception ex){
            if(trans.isActive())
                trans.rollback();
            return "Error: Unknown error occured. Please try again later";
        } finally{
            em.close();
        }

    }
    
    public String promoteUser(String username){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try{
           Users user = em.find(Users.class, username);
           if(user.getIsAdmin())
               return "Error: Already an admin";
           
           trans.begin();
           user.setIsAdmin(true);
           trans.commit();
           
           return "User promoted successfully";
        } catch(Exception ex){
            if(trans.isActive())
                trans.rollback();
            return "Error: Unknown error occured. Please try again later";
        } finally{
            em.close();
        }

    }
    
    public String demoteUser(String username){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try{
           Users user = em.find(Users.class, username);
           if(!user.getIsAdmin())
               return "Error: Already aa regular user";
           
           trans.begin();
           user.setIsAdmin(false);
           trans.commit();
           
           return "User demtoed successfully";
        } catch(Exception ex){
            if(trans.isActive())
                trans.rollback();
            return "Error: Unknown error occured. Please try again later";
        } finally{
            em.close();
        }

    }
    
    public String editUser(String username, String password, String email, String firstName, String lastName, String oldUsername) throws NoSuchAlgorithmException{
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        if((!username.trim().equals(oldUsername)) && usernameAlreadyExists(username)){
            return "Error: Username Already Exists";
        }   else if(username.length() > 10){
            return "Error: Username cannot be more than 10 characters";
        } else if(password.length() > 20)
            return "Error: Password cannot be more than 20 characters";
        else if(firstName.length() > 50 || lastName.length() > 50)
            return "Error: First name and last names must be less than 50 characters";
        try{
            Users user = em.find(Users.class, oldUsername);
            String salt = user.getPasswordSalt() != null ? user.getPasswordSalt() : "";
            if(salt.trim().equals(""))
                salt = PasswordUtil.getSalt();
            if(!username.equals(oldUsername)){
            List<Items> userItems = user.getItemsList(); 
            Users newUser = new Users(username, "", email, firstName, lastName, user.getActive(), user.getIsAdmin());
            newUser.setPasswordSalt(salt);
            newUser.setPassword(PasswordUtil.hashPassword(password+salt));
            trans.begin();     
            user.setItemsList(null); 
            for(Items item: userItems)
                item.setOwner(newUser);
            newUser.setItemsList(userItems);
            em.remove(user);
            em.persist(newUser);
            trans.commit();
            } else{
                trans.begin();
                user.setEmail(email);
                user.setFirstName(firstName);
                user.setPassword(PasswordUtil.hashPassword(password+salt));
                user.setPasswordSalt(salt);
                user.setLastName(lastName);
                trans.commit();
            }
            return "Successfully Updated User";
        }finally{
            em.close();
        }
    }
    
    public String addUser(String username, String password, String email, String firstName, String lastName) throws NoSuchAlgorithmException{
         EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        if(usernameAlreadyExists(username)){
            return "Error: Username Already Exists";
        } else if(username.length() > 10){
            return "Error: Username cannot be more than 10 characters";
        } else if(password.length() > 20)
            return "Error: Password cannot be more than 20 characters";
        else if(firstName.length() > 50 || lastName.length() > 50)
            return "Error: First name and last names must be less than 50 characters";
        try{       
            Users newUser = new Users(username, null, email, firstName, lastName, true, false);
            newUser.setPasswordSalt(PasswordUtil.getSalt());
            newUser.setPassword(PasswordUtil.hashPassword(password+newUser.getPasswordSalt()));
            trans.begin();
            em.persist(newUser);
            trans.commit();
            return "Successfully Created User";
        }finally{
            em.close();
        }
    }
    
    public String addNonActiveUser(String username, String password, String email, String firstName, String lastName) throws NoSuchAlgorithmException{
         EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        if(usernameAlreadyExists(username)){
            return "Error: Username Already Exists";
        } else if(username.length() > 10){
            return "Error: Username cannot be more than 10 characters";
        } else if(password.length() > 20)
            return "Error: Password cannot be more than 20 characters";
        else if(firstName.length() > 50 || lastName.length() > 50)
            return "Error: First name and last names must be less than 50 characters";
        try{       
            Users newUser = new Users(username, null, email, firstName, lastName, false, false);
            newUser.setPasswordSalt(PasswordUtil.getSalt());
            newUser.setPassword(PasswordUtil.hashPassword(password+newUser.getPasswordSalt()));
            trans.begin();
            em.persist(newUser);
            trans.commit();
            return "Successfully Created User";
        }finally{
            em.close();
        }
    }
    
    public String deactivateUser(String username){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try{
            Users user = em.find(Users.class, username);
            if(user.getActive() == false){
                return "Error: User is already deactivated";
            }
            trans.begin();
            user.setActive(false);
            trans.commit();
            return "User has been deactivated";
        } catch(Exception e){
            if(trans.isActive())
                trans.rollback();
            return "Error: Unknown error occured";
        }finally{
            
            em.close();
        }
    }
    
    public String activateUser(String username){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try{
            Users user = em.find(Users.class, username);
            if(user.getActive() == true){
                return "Error: User is already activated";
            }
            trans.begin();
            user.setActive(true);
            trans.commit();
            return "User has been activated";
        } catch(Exception e){
            if(trans.isActive())
                trans.rollback();
            return "Error: Unknown error occured";
        }finally{
            
            em.close();
        }
    }
    
    
    
    public boolean usernameAlreadyExists(String username){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        Users user = null;
        try{
            user = em.find(Users.class, username);
        }finally{
            em.close();
        }
        
        if(user == null)
            return false;
        else 
            return true;
    }
    
    public void addActivateAccountUuid(String username, String uuid){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try{
            Users u = em.find(Users.class, username);
            trans.begin();
            u.setActivateaccountUUID(uuid);
            trans.commit();
            
        } finally{
            em.close();
        }
    }
    
    public void deleteActivateAccountUuid(String username){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try{
            Users u = em.find(Users.class, username);
            trans.begin();
            u.setActivateaccountUUID(null);
            trans.commit();
            
        } finally{
            em.close();
        }
    }
    
    public boolean checkActivationAccountUuid(String username, String uuid){
         EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try{
            Users u = em.find(Users.class, username);
            return u.getActivateaccountUUID().equals(uuid);
               
        } finally{
            em.close();
        }
    }
    
    public void addResetPasswordUuid(String username, String uuid){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try{
            Users u = em.find(Users.class, username);
            trans.begin();
            u.setResetpasswordUUID(uuid);
            trans.commit();
            
        } finally{
            em.close();
        }
    }
    
    public void deleteResetPasswordUuid(String username){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try{
            Users u = em.find(Users.class, username);
            trans.begin();
            u.setResetpasswordUUID(null);
            trans.commit();
            
        } finally{
            em.close();
        }
    }
    
    public boolean checkResetPasswordUuid(String username, String uuid){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try{
            Users u = em.find(Users.class, username);
            return u.getResetpasswordUUID().equals(uuid);
        } finally{
            em.close();
        }
    }
    
    public String resetPassword(String username, String password){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try{
            Users u = em.find(Users.class, username);
            trans.begin();
            u.setPassword(password);
            trans.commit();
            return "All good";
        } finally{
            em.close();
        }
    }
    
}
