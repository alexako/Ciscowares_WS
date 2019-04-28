/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dlr.ciscoware_ws.v1.resources.impl;

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
@Path("/products")
public class ProductResourceImpl implements ProductResource {


    String url = "jdbc:mysql://database.alexjreyes.com:3306/java_project";
    String user = "admin";
    String pass = "mapua";

    @Override
    public List<Product> getProducts() {

        List<Product> products = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT "
                + "id, "
                + "description, "
                + "title, "
                + "name, "
                + "price, "
                + "category "
                + " FROM product");

            while (result.next()) {
                Product p = new Product();
                p.setId(result.getInt(1));
                p.setDescription(result.getString(2));
                p.setTitle(result.getString(3));
                p.setName(result.getString(4));
                p.setPrice(result.getDouble(5));
                p.setCategory(result.getString(6));
                products.add(p);
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
 
        return products;
    }

    @Override
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Product getProduct(@PathParam("id") int id) {
        
        Product p = new Product();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT "
                + "id, "
                + "title, "
                + "name, "
                + "description, "
                + "price, "
                + "category "
                + "FROM product WHERE id = " + id);

            while (result.next()) {
                p.setId(result.getInt(1));
                p.setTitle(result.getString(2));
                p.setName(result.getString(3));
                p.setDescription(result.getString(4));
                p.setPrice(result.getDouble(5));
                p.setCategory(result.getString(6));
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

 
        return p;
    }

    @Override
    @GET
    @Path("/name/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    public Product getProductByName(@PathParam("name") String name) {
        
        Product p = new Product();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT "
                + "id, "
                + "title, "
                + "name, "
                + "description, "
                + "price, "
                + "category "
                + "FROM product WHERE name like '" + name + "'");

            while (result.next()) {
                p.setId(result.getInt(1));
                p.setTitle(result.getString(2));
                p.setName(result.getString(3));
                p.setDescription(result.getString(4));
                p.setPrice(result.getDouble(5));
                p.setCategory(result.getString(6));
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

 
        return p;
    }

    @Override
    @GET
    @Path("/category/{category}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Product> getProductsByCategory(@PathParam("category") String category) {

        List<Product> products = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT "
                + "id, "
                + "title, "
                + "name, "
                + "description, "
                + "price, "
                + "category "
                + "FROM product WHERE category like '" + category + "'");

            while (result.next()) {
                Product p = new Product();
                p.setId(result.getInt(1));
                p.setTitle(result.getString(2));
                p.setName(result.getString(3));
                p.setDescription(result.getString(4));
                p.setPrice(result.getDouble(5));
                p.setCategory(result.getString(6));
                products.add(p);
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

 
        return products;
    }

    @Override
    @POST
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Product createProduct(String data) {
        JSONObject obj = new JSONObject(data);
        Product p = new Product();

        p.setName(obj.getString("name"));
        p.setDescription(obj.getString("description"));
        p.setPrice(obj.getDouble("price"));

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String insertQuery = "INSERT INTO product(name, description, price)\n" +
                "VALUES (?, ?, ?);";

            PreparedStatement preparedStmt = conn.prepareStatement(insertQuery);
            preparedStmt.setString(1, p.getName());
            preparedStmt.setString(2, p.getDescription());
            preparedStmt.setDouble(3, p.getPrice());

            if (preparedStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: product was not created");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return p;
    }

    @Override
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Product updateProduct(@PathParam("id") int id, String data) {
        JSONObject obj = new JSONObject(data);
        Product p = new Product();

        p.setName(obj.getString("name"));
        p.setDescription(obj.getString("description"));
        p.setPrice(obj.getDouble("price"));

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String updateQuery = "UPDATE product SET "
                + "name = ?, "
                + "description =  ?, "
                + "price = ? "
                + "WHERE id =" + id;

            PreparedStatement preparedStmt = conn.prepareStatement(updateQuery);
            preparedStmt.setString(1, p.getName());
            preparedStmt.setString(2, p.getDescription());
            preparedStmt.setDouble(3, p.getPrice());

            if (preparedStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: product was not updated");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return p;
    }

    @Override
    @DELETE
    @Path("{id}")
    public void removeProduct(@PathParam("id") int id) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String inventoryQuery = "DELETE FROM inventory WHERE product_id = " + id;
            String productOrderQuery = "DELETE FROM product_order WHERE product_id = " + id;
            String deleteQuery = "DELETE FROM product WHERE id =" + id;

            PreparedStatement inventoryStmt = conn.prepareStatement(inventoryQuery);
            PreparedStatement productOrderStmt = conn.prepareStatement(productOrderQuery);
            PreparedStatement preparedStmt = conn.prepareStatement(deleteQuery);

            if (inventoryStmt.executeUpdate() == 0
                    || productOrderStmt.executeUpdate() == 0
                    || preparedStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: product was not deleted");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
