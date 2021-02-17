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
import models.Categories;
import util.DBUtil;
import java.lang.Integer;
/**
 *
 * @author 839645
 */
public class CategoryDB {
    public Categories getCategory(String id){
         EntityManagerFactory emf = DBUtil.getEmFactory();
        EntityManager em = emf.createEntityManager();
        
        try{
            Categories cat = em.find(Categories.class, Integer.parseInt(id));
            return cat;
            
        } finally{
            em.close();
        }
    }
   
    public List<Categories> getAllCategories(){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        
        try{
            TypedQuery<Categories> q = em.createNamedQuery("Categories.findAll", Categories.class);
            List<Categories> cats = q.getResultList();
            if(cats == null || cats.isEmpty())
                cats = null;
            return cats;
        } finally{
            em.close();
        }
    }
    
    public String editCategory(String id, String categoryName){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try{
            Categories cat = em.find(Categories.class, Integer.parseInt(id));
            if(cat == null)
                return "Error: Category not found";
            trans.begin();
                cat.setCategoryName(categoryName);
            trans.commit();
            return "Category modified successfully";
        } catch(Exception e){
            if(trans.isActive())
                trans.rollback();
            return "Error: Unknown error occured";
        }finally{
            em.close();
        }
    }
    
    public String addCategory(String categoryName){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try{
            TypedQuery<Integer> q = em.createQuery("select max(c.categoryID) from Categories c", Integer.class);
            Integer maxId = q.getSingleResult();
            Categories cat = new Categories(maxId+1, categoryName);
          
            trans.begin();
                em.persist(cat);
            trans.commit();
            return "Category Added successfully";
        } catch(Exception e){
            if(trans.isActive())
                trans.rollback();
            System.out.println(e.getMessage());
            return "Error: Unknown error occured";
        }finally{
            em.close();
        }
    }
    

}
