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
            String salt = user != null ? user.getPasswordSalt() : "";
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
           
           return "User demoted successfully";
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
    
    public String resetPassword(String username, String password) throws NoSuchAlgorithmException{
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try{
            Users u = em.find(Users.class, username);
            trans.begin();
            u.setPassword(PasswordUtil.hashPassword(password+u.getPasswordSalt()));
            trans.commit();
            return "All good";
        } finally{
            em.close();
        }
    }
    
    public String updateProfilePic(String username, String image){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try{
            Users u = em.find(Users.class, username);
            trans.begin();
            u.setBase64Image(image);
            trans.commit();
            return "All good";
        } finally{
            em.close();
        }
    }
    
    public String getProfilePic(String username){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try{
            Users u = em.find(Users.class, username);
            if(u.getBase64Image() == null || u.getBase64Image().trim().length() == 0)
                return "data:image/png;base64,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";
            else
                return u.getBase64Image();
        } finally{
            em.close();
        }
    }
    
    public String removeProfilePic(String username){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try{
            Users u = em.find(Users.class, username);
            trans.begin();
            u.setBase64Image(null);
            trans.commit();
            return "Profile Picture removed";
        } finally{
            em.close();
        }
    }
}
