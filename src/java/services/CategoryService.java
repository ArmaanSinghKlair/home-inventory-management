/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dataaccess.CategoryDB;
import java.util.List;
import models.Categories;

/**
 *
 * @author 839645
 */
public class CategoryService {
    private CategoryDB cdb = new CategoryDB();
    
    public Categories getCategory(String id){
        return cdb.getCategory(id);
    } 
    
    public List<Categories> getAllCategories(){
        return cdb.getAllCategories();
    }
    
    public String editCategory(String id, String categoryName){
        if(isEmpty(id))
            return "Error: Unknown error occured. Please try again";
        else if(isEmpty(categoryName))
            return "Error: Category name required";
        else
            return cdb.editCategory(id, categoryName);
    }
    
    public String addCategory( String categoryName){
        if(isEmpty(categoryName))
            return "Error: Category name required";
        else
            return cdb.addCategory( categoryName);
    }
    
    private boolean isEmpty(String e){
        if(e == null || e.trim().length() == 0)
            return true;
        else 
            return false;
    }

}
