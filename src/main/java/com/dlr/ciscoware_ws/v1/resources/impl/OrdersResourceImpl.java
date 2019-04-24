/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dlr.ciscoware_ws.v1.resources.impl;

import com.dlr.ciscoware_ws.v1.resources.Branch;
import com.dlr.ciscoware_ws.v1.resources.Customer;
import com.dlr.ciscoware_ws.v1.resources.Orders;
import com.dlr.ciscoware_ws.v1.resources.OrdersResource;
import com.dlr.ciscoware_ws.v1.resources.Product;
import com.dlr.ciscoware_ws.v1.resources.ProductOrder;
import com.dlr.ciscoware_ws.v1.resources.User;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Path;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
@Path("/orders")
public class OrdersResourceImpl implements OrdersResource {


    String url = "jdbc:mysql://database.alexjreyes.com:3306/java_project";
    String user = "admin";
    String pass = "mapua";

    @Override
    public List<Orders> getAllOrders() {

        List<Orders> orders = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT\n" +
                "	o.id,\n" +
                "	u.id,\n" +
                "	u.email,\n" +
                "	u.last_name,\n" +
                "	u.first_name,\n" +
                "	c.phone_number,\n" +
                "	b.id,\n" +
                "	b.name,\n" +
                "	order_date,\n" +
                "	delivery_date,\n" +
                "	o.status,\n" +
                "	SUM(p.price * po.quantity) total,\n" +
                "FROM orders o\n" +
                "INNER JOIN product_order po\n" +
                "ON po.order_id = o.id\n" +
                "INNER JOIN product p\n" +
                "ON po.product_id = p.id\n" +
                "INNER JOIN customer c\n" +
                "ON o.customer_id = c.id\n" +
                "INNER JOIN user u\n" +
                "ON c.user_id = u.id\n" +
                "INNER JOIN branch b\n" +
                "ON o.branch_id = b.id\n" +
                "GROUP BY o.id;"); 

            while (result.next()) {

                User u = new User();
                u.setId(result.getInt(2));
                u.setEmail(result.getString(3));
                u.setLastName(result.getString(4));
                u.setFirstName(result.getString(5));

                Customer c = new Customer();
                c.setUserId(u);
                c.setPhoneNumber(result.getString(6));
                
                Branch b = new Branch();
                b.setId(result.getInt(7));
                b.setName(result.getString(8));

                Orders o = new Orders();
                o.setId(result.getInt(1));
                o.setOrderDate(result.getDate(9));
                o.setDeliveryDate(result.getDate(10));
                o.setStatus(result.getString(11));
                o.setTotalCost(result.getDouble(12));
                o.setCustomerId(c);
                o.setBranchId(b);

                orders.add(o);
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
 
        return orders;
    }

    @Override
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Orders getOrder(@PathParam("id") String id) {

        Orders o = new Orders();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT\n" +
                "	o.id,\n" +
                "	u.id,\n" +
                "	u.email,\n" +
                "	u.last_name,\n" +
                "	u.first_name,\n" +
                "	c.phone_number,\n" +
                "	b.id,\n" +
                "	b.name,\n" +
                "	order_date,\n" +
                "	delivery_date,\n" +
                "	o.status,\n" +
                "	SUM(p.price * po.quantity) total\n" +
                "FROM orders o\n" +
                "INNER JOIN product_order po\n" +
                "ON po.order_id = o.id\n" +
                "INNER JOIN product p\n" +
                "ON po.product_id = p.id\n" +
                "INNER JOIN customer c\n" +
                "ON o.customer_id = c.id\n" +
                "INNER JOIN user u\n" +
                "ON c.user_id = u.id\n" +
                "INNER JOIN branch b\n" +
                "ON o.branch_id = b.id\n" +
                "WHERE o.id = " + id + "\n" + 
                "GROUP BY o.id");

            while (result.next()) {

                ProductOrderResourceImpl res = new ProductOrderResourceImpl();
                List<ProductOrder> productOrders = res.getProductOrdersByOrder(result.getInt(1));

                User u = new User();
                u.setId(result.getInt(2));
                u.setEmail(result.getString(3));
                u.setLastName(result.getString(4));
                u.setFirstName(result.getString(5));

                Customer c = new Customer();
                c.setUserId(u);
                c.setPhoneNumber(result.getString(6));

                Branch b = new Branch();
                b.setId(result.getInt(7));
                b.setName(result.getString(8));

                o.setId(result.getInt(1));
                o.setOrderDate(result.getDate(9));
                o.setDeliveryDate(result.getDate(10));
                o.setStatus(result.getString(11));
                o.setTotalCost(result.getDouble(12));
                o.setCustomerId(c);
                o.setBranchId(b);
                o.setProductOrders(productOrders);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
 
        return o;
    }

    @Override
    @GET
    @Path("/user/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Orders> getOrdersByUser(@PathParam("id") int id) {
        List<Orders> orders = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT\n" +
                "	o.id,\n" +
                "	u.id,\n" +
                "	u.email,\n" +
                "	u.last_name,\n" +
                "	u.first_name,\n" +
                "	c.phone_number,\n" +
                "	b.id,\n" +
                "	b.name,\n" +
                "	order_date,\n" +
                "	delivery_date,\n" +
                "	o.status,\n" +
                "	SUM(p.price * po.quantity) total\n" +
                "FROM orders o\n" +
                "INNER JOIN product_order po\n" +
                "ON po.order_id = o.id\n" +
                "INNER JOIN product p\n" +
                "ON po.product_id = p.id\n" +
                "INNER JOIN customer c\n" +
                "ON o.customer_id = c.id\n" +
                "INNER JOIN user u\n" +
                "ON c.user_id = u.id\n" +
                "INNER JOIN branch b\n" +
                "ON o.branch_id = b.id\n" +
                "WHERE u.id = " + id + "\n" +
                "GROUP BY o.id");

            while (result.next()) {

                User u = new User();
                u.setId(result.getInt(2));
                u.setEmail(result.getString(3));
                u.setLastName(result.getString(4));
                u.setFirstName(result.getString(5));

                Customer c = new Customer();
                c.setUserId(u);
                c.setPhoneNumber(result.getString(6));
                
                Branch b = new Branch();
                b.setId(result.getInt(7));
                b.setName(result.getString(8));

                Orders o = new Orders();
                o.setId(result.getInt(1));
                o.setOrderDate(result.getDate(9));
                o.setDeliveryDate(result.getDate(10));
                o.setStatus(result.getString(11));
                o.setTotalCost(result.getDouble(12));
                o.setCustomerId(c);
                o.setBranchId(b);

                orders.add(o);
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
            
        return orders;
    }

    @Override
    @POST
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Orders createOrder(String data) {
        JSONObject obj = new JSONObject(data);

        Customer c = new Customer();
        c.setId(obj.getInt("customerId"));

        Branch b = new Branch();
        b.setId(obj.getInt("branchId"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());            
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date deliveryDate = calendar.getTime();

        Orders o = new Orders();
        o.setCustomerId(c);
        o.setBranchId(b);
        o.setOrderDate(new Date());
        o.setDeliveryDate(new java.sql.Date(deliveryDate.getTime()));
        o.setTotalCost(obj.getDouble("totalCost"));
        o.setStatus("Pending");

        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String insertQuery = "INSERT INTO orders("
                + "customer_id,"
                + "branch_id,"
                + "order_date,"
                + "delivery_date,"
                + "total_cost,"
                + "status)\n" +
                "VALUES (?, ?, ?, ?, ?, ?);";


            PreparedStatement preparedStmt = conn.prepareStatement(insertQuery);
            preparedStmt.setInt(1, o.getCustomerId().getId());
            preparedStmt.setInt(2, o.getBranchId().getId());
            preparedStmt.setString(3, format.format(o.getOrderDate()));
            preparedStmt.setString(4, format.format(o.getDeliveryDate()));
            preparedStmt.setDouble(5, o.getTotalCost());
            preparedStmt.setString(6, o.getStatus());

            if (preparedStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: order was not created");
            }

            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT\n" +
                "	MAX(id)\n" +
                "FROM orders");

            while (result.next()) {
                o.setId(result.getInt(1));
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }


        return o;
    }

    @Override
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Orders updateOrder(@PathParam("id") int id, String data) {
        JSONObject obj = new JSONObject(data);

        Customer c = new Customer();
        c.setId(obj.getInt("customerId"));

        Branch b = new Branch();
        b.setId(obj.getInt("branchId"));

        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
        Date oDate = new Date(),
            dDate = new Date();
        try {
            oDate = format.parse(obj.getString("orderDate"));
            dDate = format.parse(obj.getString("deliveryDate"));
        } catch (ParseException e) {

        }

        Orders o = new Orders();
        o.setCustomerId(c);
        o.setBranchId(b);
        o.setDeliveryDate(dDate);
        o.setTotalCost(obj.getDouble("totalCost"));
        o.setStatus(obj.getString("status"));

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String insertQuery = "UPDATE orders SET "
                + "customer_id = ?,"
                + "branch_id = ?,"
                + "delivery_date = ?,"
                + "total_cost = ?,"
                + "status = ?;";

            PreparedStatement preparedStmt = conn.prepareStatement(insertQuery);
            preparedStmt.setInt(1, o.getCustomerId().getId());
            preparedStmt.setInt(2, o.getBranchId().getId());
            preparedStmt.setString(3, format.format(o.getDeliveryDate()));
            preparedStmt.setDouble(4, o.getTotalCost());
            preparedStmt.setString(5, o.getStatus());

            if (preparedStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: order was not created");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return o;
    }

    @Override
    @DELETE
    @Path("{id}")
    public void removeOrder(@PathParam("id") int id) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String deleteQuery = "DELETE FROM orders WHERE id =" + id;

            PreparedStatement preparedStmt = conn.prepareStatement(deleteQuery);

            if (preparedStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: order was not deleted");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
