/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Items;
import services.InventoryService;

/**
 *
 * @author 839645
 */
public class InventoryServlet extends HttpServlet {

     
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sess = request.getSession();
        String username = (sess.getAttribute("admin") != null) ? (String) sess.getAttribute("admin") :(String) sess.getAttribute("regularUser");
        sess.setAttribute("items", new InventoryService().getAllItems(username));
        sess.setAttribute("categories", new InventoryService().getCategories());
        this.getServletContext().getRequestDispatcher("/WEB-INF/regularUser.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String url = "/WEB-INF/regularUser.jsp";
        HttpSession sess =request.getSession();
        String username = (sess.getAttribute("admin") != null) ? (String) sess.getAttribute("admin") :(String) sess.getAttribute("regularUser");        
        InventoryService is = new InventoryService();
        switch(action){
            case "deleteItem":
                String itemId = request.getParameter("itemId");
                String deleteStatus = is.deleteItem(itemId, username);
                
                if(deleteStatus.toLowerCase().startsWith("error")){
                    request.setAttribute("errMsg", deleteStatus);
                } else{
                    request.setAttribute("infoMsg", deleteStatus);
                }
                
                break;
            case "addItem":
                String categoryId= request.getParameter("categoryId");
                String itemName= request.getParameter("itemName");
                String itemPrice = request.getParameter("itemPrice");
                username = (sess.getAttribute("admin") != null) ? (String) sess.getAttribute("admin") :(String) sess.getAttribute("regularUser");

                String addStatus = is.addItem(categoryId, itemName, itemPrice, username);
                
                if(addStatus.toLowerCase().startsWith("error")){
                    request.setAttribute("errMsg", addStatus.substring(7));
                    request.setAttribute("categoryId", categoryId);
                    request.setAttribute("itemName", itemName);
                    request.setAttribute("itemPrice", itemPrice);
                } else{
                    request.setAttribute("infoMsg", addStatus);
                }
                break;
            case "editItemBtnClick":
                String itemID = request.getParameter("itemId");
                Items item = is.getItemById(itemID);
                if(item.getOwner().getUsername().equals(username)){
                    request.setAttribute("editModeItem", true);
                    request.setAttribute("categoryId", item.getCategory().getCategoryID());
                    request.setAttribute("itemName", item.getItemName());
                    request.setAttribute("itemPrice", item.getPrice());
                    request.setAttribute("itemID", itemID);
                }
                else{
                    request.setAttribute("errMsg", "Item does not belong to current user");
                }
                break;
                
            case "editItem":
                categoryId= request.getParameter("categoryId");
                itemName= request.getParameter("itemName");
                itemPrice = request.getParameter("itemPrice");
                itemID = request.getParameter("itemID");

                String editStatus = is.editItem(categoryId, itemName, itemPrice, itemID, username);
                
                if(editStatus.toLowerCase().startsWith("error")){
                    request.setAttribute("errMsg", editStatus.substring(7));
                    request.setAttribute("categoryId", categoryId);
                    request.setAttribute("itemName", itemName);
                    request.setAttribute("itemPrice", itemPrice);
                    request.setAttribute("itemID", itemID);
                    request.setAttribute("editModeItem", true);

                } else{
                    request.setAttribute("infoMsg", editStatus);
                }
                
                break;
        }
        sess.setAttribute("items", new InventoryService().getAllItems(username));

        this.getServletContext().getRequestDispatcher(url).forward(request, response);
        
        
    }
}
