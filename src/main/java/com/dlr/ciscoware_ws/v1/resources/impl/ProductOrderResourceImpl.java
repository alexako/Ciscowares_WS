/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dlr.ciscoware_ws.v1.resources.impl;

import com.dlr.ciscoware_ws.v1.resources.Orders;
import com.dlr.ciscoware_ws.v1.resources.Product;
import com.dlr.ciscoware_ws.v1.resources.ProductOrder;
import com.dlr.ciscoware_ws.v1.resources.ProductOrderResource;
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
@Path("/product-orders")
public class ProductOrderResourceImpl implements ProductOrderResource {


    String url = "jdbc:mysql://database.alexjreyes.com:3306/java_project";
    String user = "admin";
    String pass = "mapua";

    @Override
    public List<ProductOrder> getProductOrders() {

        List<ProductOrder> productOrders = new ArrayList<>();

        Connection conn = null;
        ResultSet result = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            result = stmt.executeQuery("SELECT "
                + "po.id,\n"
                + "po.product_id,\n"
                + "po.order_id,\n"
                + "po.quantity,\n"
                + "p.name,\n"
                + "p.description,\n"
                + "p.price\n"
                + "FROM product_order po\n"
                + "INNER JOIN product p\n"
                + "ON po.product_id = p.id");

            while (result.next()) {
                Product p = new Product();
                p.setId(result.getInt(2));
                p.setName(result.getString(5));
                p.setDescription(result.getString(6));
                p.setPrice(result.getDouble(7));

                Orders o = new Orders();
                o.setId(result.getInt(3));

                ProductOrder po = new ProductOrder();
                po.setId(result.getInt(1));
                po.setProductId(p);
                po.setOrderId(o);
                po.setQuantity(result.getInt(4));
                productOrders.add(po);
            }
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (result != null) try { result.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { conn.close(); } catch (SQLException logOrIgnore) {}
        }
 
        return productOrders;
    }

    @Override
    @GET
    @Path("/order/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ProductOrder> getProductOrdersByOrder(@PathParam("id") int id) {

        List<ProductOrder> productOrders = new ArrayList<>();

        Connection conn = null;
        ResultSet result = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            result = stmt.executeQuery("SELECT "
                + "po.id,\n"
                + "po.product_id,\n"
                + "po.order_id,\n"
                + "po.quantity,\n"
                + "p.name,\n"
                + "p.description,\n"
                + "p.price\n"
                + "FROM product_order po\n"
                + "INNER JOIN product p\n"
                + "ON po.product_id = p.id\n"
                + "WHERE po.order_id = " + id);

            while (result.next()) {
                Product p = new Product();
                p.setId(result.getInt(2));
                p.setName(result.getString(5));
                p.setDescription(result.getString(6));
                p.setPrice(result.getDouble(7));

                Orders o = new Orders();
                o.setId(result.getInt(3));

                ProductOrder po = new ProductOrder();
                po.setId(result.getInt(1));
                po.setProductId(p);
                po.setOrderId(o);
                po.setQuantity(result.getInt(4));
                productOrders.add(po);
            }
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (result != null) try { result.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { conn.close(); } catch (SQLException logOrIgnore) {}
        }
 
        return productOrders;
    }

    @Override
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public ProductOrder getProductOrder(@PathParam("id") int id) {
        
        ProductOrder po = new ProductOrder();

        Connection conn = null;
        ResultSet result = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            result = stmt.executeQuery("SELECT * FROM product_order WHERE id = " + id);

            while (result.next()) {
                Product p = new Product();
                p.setId(result.getInt(2));

                Orders o = new Orders();
                o.setId(result.getInt(3));

                po.setId(result.getInt(1));
                po.setProductId(p);
                po.setOrderId(o);
                po.setQuantity(result.getInt(4));
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (result != null) try { result.close(); } catch (SQLException logOrIgnore) {}
            if (conn != null) try { conn.close(); } catch (SQLException logOrIgnore) {}
        }

 
        return po;
    }

    @Override
    @POST
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ProductOrder createProductOrder(String data) {
        JSONObject obj = new JSONObject(data);
        Product p = new Product();
        p.setId(obj.getInt("productId"));

        Orders o = new Orders();
        o.setId(obj.getInt("orderId"));

        ProductOrder po = new ProductOrder();
        po.setProductId(p);
        po.setOrderId(o);
        po.setQuantity(obj.getInt("quantity"));

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String insertQuery = "INSERT INTO product_order(product_id, order_id, quantity)\n" +
                "VALUES (?, ?, ?);";

            PreparedStatement preparedStmt = conn.prepareStatement(insertQuery);
            preparedStmt.setInt(1, po.getProductId().getId());
            preparedStmt.setInt(2, po.getOrderId().getId());
            preparedStmt.setInt(3, po.getQuantity());

            if (preparedStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: product order was not created");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return po;
    }

    @Override
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ProductOrder updateProductOrder(@PathParam("id") int id, String data) {
        JSONObject obj = new JSONObject(data);
        Product p = new Product();
        p.setId(obj.getInt("productId"));

        Orders o = new Orders();
        o.setId(obj.getInt("orderId"));

        ProductOrder po = new ProductOrder();
        po.setProductId(p);
        po.setOrderId(o);
        po.setQuantity(obj.getInt("quantity"));

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String updateQuery = "UPDATE product_order SET "
                + "product_id = ?, "
                + "order_id = ?, "
                + "quantity = ? "
                + "WHERE id = " + id;

            PreparedStatement preparedStmt = conn.prepareStatement(updateQuery);
            preparedStmt.setInt(1, po.getProductId().getId());
            preparedStmt.setInt(2, po.getOrderId().getId());
            preparedStmt.setInt(3, po.getQuantity());

            if (preparedStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: product order was not created");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return po;
    }

    @Override
    @DELETE
    @Path("{id}")
    public void removeProductOrder(@PathParam("id") int id) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String deleteQuery = "DELETE FROM product_order WHERE id =" + id;

            PreparedStatement preparedStmt = conn.prepareStatement(deleteQuery);

                if (preparedStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: product order was not deleted");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
