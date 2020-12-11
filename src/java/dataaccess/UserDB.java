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
