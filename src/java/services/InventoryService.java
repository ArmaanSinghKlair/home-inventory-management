/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dataaccess.ItemsDB;
import java.util.*;
import models.Categories;
import models.Items;

/**
 *
 * @author 839645
 */
public class InventoryService {
  
    public List<Items> getAllItems(String username){
        if( isEmpty(username))
            return null;
        else
            return new ItemsDB().getAllItems(username);
    }

    public Items getItemById(String id){
        if(isEmpty(id))
            return null;
        return new ItemsDB().getItemById(id);
    }

    public String deleteItem(String itemId, String username){
        if( username == null || username.trim().length() == 0)
            return "Error: Username not found. Please login again. Sorry for the inconvinience";
        else if( itemId == null || itemId.trim().length() == 0)
            return "Error: Unknown error occured. Please refresh and try again";
        else{
            return new ItemsDB().deleteItem(itemId, username);
        }
    }
    
    public String addItem(String categoryId, String itemName, String itemPrice, String username){
        if( isEmpty(username))
            return "Error: Username not found. Please login again. Sorry for the inconvinience";
        else if( isEmpty(categoryId))
            return "Error: Category Id is required";
        else if( isEmpty(itemName))
            return "Error: Item Name is required";
        else if( isEmpty(itemPrice))
            return "Error: Item Price is required";
        else if(Double.parseDouble(itemPrice) <= 0)
            return "Error: Item price cannot be negative";
        else{
            return new ItemsDB().addItem(categoryId, itemName, itemPrice, username);
        }
    }
    
    public String editItem(String categoryId, String itemName, String itemPrice, String itemID, String username){
        if( isEmpty(categoryId))
            return "Error: Category Id is required";
        else if( isEmpty(itemID))
            return "Error: Item Id is required";
        else if( isEmpty(itemName))
            return "Error: Item Name is required";
        else if( isEmpty(itemPrice))
            return "Error: Item Price is required";
        else if(Double.parseDouble(itemPrice) <= 0)
            return "Error: Item price cannot be negative";
        else{
            return new ItemsDB().editItem(categoryId, itemName, itemPrice, itemID, username);
        }
    }
    
        public List<Categories> getCategories(){
            return new ItemsDB().getCategories();
        }
        
    private boolean isEmpty(String e){
        if(e == null || e.trim().length() == 0)
            return true;
        else 
            return false;
    }
}
