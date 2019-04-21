/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dlr.ciscoware_ws.v1.resources.impl;

import com.dlr.ciscoware_ws.v1.resources.Branch;
import com.dlr.ciscoware_ws.v1.resources.Inventory;
import com.dlr.ciscoware_ws.v1.resources.InventoryResource;
import com.dlr.ciscoware_ws.v1.resources.Product;
import com.dlr.ciscoware_ws.v1.resources.ProductResource;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Path;
import java.sql.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;

/**
 *
 * @author alex
 */
@Path("/v1/inventory")
public class InventoryResourceImpl implements InventoryResource {


    String url = "jdbc:mysql://database.alexjreyes.com:3306/java_project";
    String user = "admin";
    String pass = "mapua";

    @Override
    public List<Inventory> getInventory() {

        List<Inventory> inventory = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT\n" +
                "    i.id,\n" +
                "    p.id,\n" +
                "    p.name,\n" +
                "    p.description,\n" +
                "    p.price,\n" +
                "    i.quantity,\n" +
                "    b.id,\n" +
                "    b.name\n" +
                "FROM inventory i\n" +
                "LEFT JOIN product p\n" +
                "ON i.product_id = p.id\n" +
                "LEFT JOIN branch b\n" +
                "ON i.branch_id = b.id;");

            while (result.next()) {
                Product p = new Product();
                p.setId(result.getInt(2));
                p.setName(result.getString(3));
                p.setDescription(result.getString(4));
                p.setPrice(result.getDouble(5));

                Branch b = new Branch();
                b.setId(result.getInt(7));
                b.setName(result.getString(8));

                Inventory i = new Inventory();
                i.setId(result.getInt(1));
                i.setProductId(p);
                i.setBranch(b);
                i.setBranchId(b.getId());
                i.setQuantity(result.getInt(6));
                inventory.add(i);
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
 
        return inventory;
    }

    @Override
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Inventory> getProduct(@PathParam("id") int id) {
        
        List<Inventory> inventory = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT\n" +
                "    i.id,\n" +
                "    p.id,\n" +
                "    p.name,\n" +
                "    p.description,\n" +
                "    p.price,\n" +
                "    i.quantity,\n" +
                "    b.id,\n" +
                "    b.name\n" +
                "FROM inventory i\n" +
                "LEFT JOIN product p\n" +
                "ON i.product_id = p.id\n" +
                "LEFT JOIN branch b\n" +
                "ON i.branch_id = b.id\n" +
                "WHERE i.product_id = " + id);

            while (result.next()) {
                Product p = new Product();
                p.setId(result.getInt(2));
                p.setName(result.getString(3));
                p.setDescription(result.getString(4));
                p.setPrice(result.getDouble(5));

                Branch b = new Branch();
                b.setId(result.getInt(7));
                b.setName(result.getString(8));

                Inventory i = new Inventory();
                i.setId(result.getInt(1));
                i.setProductId(p);
                i.setBranchId(b.getId());
                i.setBranch(b);
                i.setQuantity(result.getInt(6));
                inventory.add(i);
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

 
        return inventory;
    }

    @Override
    @POST
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Inventory createInventory(String data) {
        JSONObject obj = new JSONObject(data);
        Product p = new Product();
        p.setId(obj.getInt("productId"));

        Inventory i = new Inventory();
        i.setProductId(p);
        i.setQuantity(obj.getInt("quantity"));
        i.setBranchId(obj.getInt("branchId"));

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String insertQuery = "INSERT INTO inventory(product_id, quantity, branch)\n" +
                "VALUES (?, ?, ?);";

            PreparedStatement preparedStmt = conn.prepareStatement(insertQuery);
            preparedStmt.setInt(1, p.getId());
            preparedStmt.setInt(2, i.getQuantity());
            preparedStmt.setInt(3, i.getBranchId());

            if (preparedStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: product was not created");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return i;
    }

    @Override
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Inventory updateInventory(@PathParam("id") int id, String data) {
        JSONObject obj = new JSONObject(data);

        Product p = new Product();
        p.setId(obj.getInt("productId"));

        Inventory i = new Inventory();
        i.setProductId(p);
        i.setQuantity(obj.getInt("quantity"));
        i.setBranchId(obj.getInt("branchId"));

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String updateQuery = "UPDATE inventory SET "
                + "product_id = ?, "
                + "quantity =  ?, "
                + "branch_id = ? "
                + "WHERE id =" + id;

            PreparedStatement preparedStmt = conn.prepareStatement(updateQuery);
            preparedStmt.setInt(1, p.getId());
            preparedStmt.setInt(2, i.getQuantity());
            preparedStmt.setInt(3, i.getBranchId());

            if (preparedStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: inventory was not updated");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return i;
    }

    @Override
    @DELETE
    @Path("{id}")
    public void removeInventory(@PathParam("id") int id) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String deleteQuery = "DELETE FROM inventory WHERE id =" + id;

            PreparedStatement preparedStmt = conn.prepareStatement(deleteQuery);

            if (preparedStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: inventory was not deleted");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
