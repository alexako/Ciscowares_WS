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

                Orders o = new Orders();
                o.setId(result.getInt(1));
                o.setOrderDate(result.getDate(7));
                o.setDeliveryDate(result.getDate(8));
                o.setStatus(result.getString(9));
                o.setTotalCost(result.getDouble(10));
                o.setCustomerId(c);

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
    public Orders getOrder(@PathParam("id") int id) {

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
                "GROUP BY o.id\n" +
                "WHERE o.id = " + id); 

            while (result.next()) {

                User u = new User();
                u.setId(result.getInt(2));
                u.setEmail(result.getString(3));
                u.setLastName(result.getString(4));
                u.setFirstName(result.getString(5));

                Customer c = new Customer();
                c.setUserId(u);
                c.setPhoneNumber(result.getString(6));

                o.setId(result.getInt(1));
                o.setOrderDate(result.getDate(7));
                o.setDeliveryDate(result.getDate(8));
                o.setStatus(result.getString(9));
                o.setTotalCost(result.getDouble(10));
                o.setCustomerId(c);
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
            ResultSet result = stmt.executeQuery("SELECT \n" +
                "    o.id,\n" +
                "    o.order_date,\n" +
                "    o.delivery_date,\n" +
                "    o.total_cost,\n" +
                "    p.id,\n" +
                "    p.name,\n" +
                "    p.description,\n" +
                "    po.quantity,\n" +
                "    p.price,\n" +
                "    c.user_id,\n" +
                "    u.email,\n" +
                "    c.phone_number\n" +
                "FROM orders o\n" +
                "LEFT JOIN product_order po\n" +
                "ON po.order_id = o.id\n" +
                "LEFT JOIN product p\n" +
                "ON po.product_id = p.id\n" +
                "LEFT JOIN customer c\n" +
                "ON o.customer_id = c.id\n" +
                "LEFT JOIN user u\n" +
                "ON c.user_id = u.id\n" +
                "WHERE c.user_id = " + id); 

            while (result.next()) {
                Product p = new Product();
                p.setId(result.getInt(5));
                p.setName(result.getString(6));
                p.setDescription(result.getString(7));
                p.setPrice(result.getDouble(9));

                ProductOrder po = new ProductOrder();
                po.setQuantity(result.getInt(8));

                User u = new User();
                u.setId(result.getInt(10));
                u.setEmail(result.getString(11));

                Customer c = new Customer();
                c.setUserId(u);
                c.setPhoneNumber(result.getString(12));

                Orders o = new Orders();
                o.setId(result.getInt(1));
                o.setOrderDate(result.getDate(2));
                o.setDeliveryDate(result.getDate(3));
                o.setTotalCost(result.getDouble(4));
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
