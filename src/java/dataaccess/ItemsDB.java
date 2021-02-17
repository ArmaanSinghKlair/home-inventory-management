/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import models.*;
import util.DBUtil;

/**
 *
 * @author 839645
 */
public class ItemsDB {
    public List<Items> getAllItems(String username){
        EntityManagerFactory emf = DBUtil.getEmFactory();
        EntityManager em = emf.createEntityManager();
        
        try{
            List<Items> items = em.find(Users.class, username).getItemsList();
            return items;
            
        } finally{
            em.close();
        }
    }
    
    public String deleteItem(String itemId, String username){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try{
            Items item = em.find(Items.class, Integer.parseInt(itemId));
            if(item.getOwner().getUsername().equals(username)){
                trans.begin();
                
                Users user = em.find(Users.class, username);
                user.getItemsList().remove(item);
                em.remove(item);
                
                trans.commit();
                return "Successfully Removed Item";
            } else{
                return "Error: Item does not belong to current user.";
            }
        } catch(Exception e){
            if( trans.isActive() )
                trans.rollback();
            return "Error: "+e.getMessage();
        }finally{
            em.close();
        }
    }
    
    public String addItem(String categoryId, String itemName, String itemPrice, String username){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try{
            TypedQuery<Integer> q = em.createQuery("SELECT MAX(i.itemID) FROM Items i", Integer.class);
            
            Integer maxId = q.getSingleResult();
            Items newItem = new Items(maxId+1, itemName, Integer.parseInt(itemPrice));
            Categories c = em.find(Categories.class, Integer.parseInt(categoryId));
            Users owner = em.find(Users.class, username);
            newItem.setCategory(c);
            newItem.setOwner(owner);
            
            trans.begin();
            em.persist(newItem);
            owner.getItemsList().add(newItem);
            trans.commit();
            return "Item added successfully";
        }catch(Exception e){
            return "Error: "+e.getMessage();
        }
            finally{
            em.close();
        }
    }

    public String editItem(String categoryId, String itemName, String itemPrice, String itemID, String username){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try{                
            Items item = em.find(Items.class, Integer.parseInt(itemID));

            if(item.getOwner().getUsername().equals(username)){
                Categories category = em.find(Categories.class, Integer.parseInt(categoryId));
                trans.begin();
                item.setCategory(category);
                item.setItemName(itemName);
                item.setPrice(Double.parseDouble(itemPrice));
                trans.commit();
                return "Item edited successfully";
            } else{
                return "Error: Item does not belong to current user.";
            }
        }catch(Exception e){
            return "Error: "+e.getMessage();
        }
            finally{
            em.close();
        }
    }
    
    public List<Categories> getCategories(){
                EntityManager em = DBUtil.getEmFactory().createEntityManager();
                TypedQuery<Categories> q = em.createNamedQuery("Categories.findAll", Categories.class);
                
                try{
                    List<Categories> categories = q.getResultList();
                    
                    if(categories == null || categories.size() == 0)
                        return null;
                    else{
                        return categories;
                    }
                   
                } finally{
                    em.close();
                }
    }
    
    public Items getItemById(String id){
         EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try{
            Items item = em.find(Items.class, Integer.parseInt(id));
            return item;
        }catch(Exception e){
            return null;
        }
            finally{
            em.close();
        }
    }
}
