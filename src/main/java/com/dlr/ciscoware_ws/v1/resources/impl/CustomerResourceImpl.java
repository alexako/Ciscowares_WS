/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dlr.ciscoware_ws.v1.resources.impl;

import com.dlr.ciscoware_ws.v1.resources.Customer;
import com.dlr.ciscoware_ws.v1.resources.CustomerAddress;
import com.dlr.ciscoware_ws.v1.resources.CustomerResource;
import com.dlr.ciscoware_ws.v1.resources.User;
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
@Path("/customers")
public class CustomerResourceImpl implements CustomerResource {


    String url = "jdbc:mysql://database.alexjreyes.com:3306/java_project";
    String user = "admin";
    String pass = "mapua";

    @Override
    public List<Customer> getCustomers() {

        List<Customer> customers = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT \n" +
                "    c.id,\n" +
                "    u.id,\n" +
                "    u.last_name,\n" +
                "    u.first_name,\n" +
                "    u.email,\n" +
                "    u.role,\n" +
                "    c.phone_number,\n" +
                "    ca.street,\n" +
                "    ca.city,\n" +
                "    ca.province,\n" +
                "    ca.country,\n" +
                "    ca.zip_code\n" +
                "FROM customer c\n" +
                "LEFT JOIN\n" +
                "user u\n" +
                "ON c.user_id = u.id\n" +
                "INNER JOIN\n" +
                "customer_address ca\n" +
                "ON ca.customer_id = c.id;");

            while (result.next()) {
                User u = new User();
                u.setId(result.getInt(2));
                u.setLastName(result.getString(3));
                u.setFirstName(result.getString(4));
                u.setEmail(result.getString(5));
                u.setRole(result.getString(6));

                List<CustomerAddress> address = new ArrayList<>();
                CustomerAddress ca = new CustomerAddress();
                ca.setStreet(result.getString(8));
                ca.setCity(result.getString(9));
                ca.setProvince(result.getString(10));
                ca.setCountry(result.getString(11));
                ca.setZipCode(result.getString(12));

                Customer c = new Customer();
                c.setId(result.getInt(1));
                c.setPhoneNumber(result.getString(7));
                c.setUserId(u);
                c.setCustomerAddressCollection(address);
                customers.add(c);
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
 
        return customers;
    }

    @Override
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Customer getCustomer(@PathParam("id") int id) {
        
        Customer c = new Customer();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT \n" +
                "    c.id,\n" +
                "    u.id,\n" +
                "    u.last_name,\n" +
                "    u.first_name,\n" +
                "    u.email,\n" +
                "    u.role,\n" +
                "    c.phone_number,\n" +
                "    ca.street,\n" +
                "    ca.city,\n" +
                "    ca.province,\n" +
                "    ca.country,\n" +
                "    ca.zip_code\n" +
                "FROM customer c\n" +
                "INNER JOIN\n" +
                "user u\n" +
                "ON c.user_id = u.id\n" +
                "INNER JOIN\n" +
                "customer_address ca\n" +
                "ON ca.customer_id = c.id\n" +
                "WHERE c.id=" + id);

            while (result.next()) {
                User u = new User();
                u.setId(result.getInt(2));
                u.setLastName(result.getString(3));
                u.setFirstName(result.getString(4));
                u.setEmail(result.getString(5));
                u.setRole(result.getString(6));

                CustomerAddress ca = new CustomerAddress();
                ca.setStreet(result.getString(8));
                ca.setCity(result.getString(9));
                ca.setProvince(result.getString(10));
                ca.setCountry(result.getString(11));
                ca.setZipCode(result.getString(12));

                c.setId(result.getInt(1));
                c.setPhoneNumber(result.getString(7));
                c.setUserId(u);
                c.setCustomerAddress(ca);
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

 
        return c;
    }

    @Override
    @POST
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Customer createCustomer(String data) {
        JSONObject obj = new JSONObject(data);

        User u = new User();
        u.setFirstName(obj.getString("firstName"));
        u.setLastName(obj.getString("lastName"));
        u.setEmail(obj.getString("email"));
        u.setRole(obj.getString("role"));

        CustomerAddress ca = new CustomerAddress();
        ca.setStreet(obj.getString("street"));
        ca.setCity(obj.getString("city"));
        ca.setProvince(obj.getString("province"));
        ca.setCountry(obj.getString("country"));
        ca.setZipCode(obj.getString("zipCode"));
        List<CustomerAddress> address = new ArrayList<>();
        address.add(ca);

        Customer c = new Customer();
        c.setPhoneNumber(obj.getString("phoneNumber"));
        c.setId(u.getId());
        c.setCustomerAddressCollection(address);
        c.setUserId(u);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String userQuery = "INSERT INTO user(email, first_name, last_name, role)\n" +
                "VALUES (?, ?, ?, ?);"; 
            String customerQuery = "INSERT INTO customer(user_id, phone_number)\n" +
                "VALUES (LAST_INSERT_ID(), ?);";
            String cAddressQuery = "INSERT INTO customer_address"
                + "(customer_id, street, city, province, zip_code, country)\n" +
                "VALUES (\n" +
                "    LAST_INSERT_ID(),\n" +
                "    ?,\n" +
                "    ?,\n" +
                "    ?,\n" +
                "    ?,\n" +
                "    ?\n" +
                ");";
            String passwordQuery = "INSERT INTO password(user_id, content)\n" +
                "VALUES (\n" +
                "    (SELECT c.user_id FROM customer c\n" +
                "        LEFT JOIN customer_address ca\n" +
                "        ON ca.customer_id = c.id\n" +
                "        WHERE ca.id = LAST_INSERT_ID()),\n" +
                "    ?\n" +
                ");";

            PreparedStatement userStmt = conn.prepareStatement(userQuery);
            userStmt.setString(1, u.getEmail());
            userStmt.setString(2, u.getFirstName());
            userStmt.setString(3, u.getLastName());
            userStmt.setString(4, u.getRole());

            PreparedStatement customerStmt = conn.prepareStatement(customerQuery);
            customerStmt.setString(1, c.getPhoneNumber());

            PreparedStatement cAddressStmt = conn.prepareStatement(cAddressQuery);
            cAddressStmt.setString(1, ca.getStreet());
            cAddressStmt.setString(2, ca.getCity());
            cAddressStmt.setString(3, ca.getProvince());
            cAddressStmt.setString(4, ca.getCountry());
            cAddressStmt.setString(5, ca.getZipCode());

            PreparedStatement passwordStmt = conn.prepareStatement(passwordQuery);
            passwordStmt.setString(1, obj.getString("password"));

            if (userStmt.executeUpdate() == 0
                    || customerStmt.executeUpdate() == 0
                    || cAddressStmt.executeUpdate() == 0
                    || passwordStmt.executeUpdate() == 0) {
                conn.rollback();
                throw new Exception("ERROR: customer was not created");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return c;
    }

    @Override
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Customer updateCustomer(@PathParam("id") int id, String data) {
        JSONObject obj = new JSONObject(data);

        User u = new User();
        u.setFirstName(obj.getString("firstName"));
        u.setLastName(obj.getString("lastName"));
        u.setEmail(obj.getString("email"));
        u.setRole(obj.getString("role"));

        Customer c = new Customer();
        c.setPhoneNumber(obj.getString("phoneNumber"));
        c.setUserId(u);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String userQuery = "UPDATE user SET "
                + "email = ?, "
                + "first_name = ?, "
                + "last_name = ?, "
                + "role = ? "
                + "WHERE id = " + id;
            String customerQuery = "UPDATE customer" + "\n"
                + "SET phone_number = ? "
                + "WHERE user_id = " + id;

            PreparedStatement userStmt = conn.prepareStatement(userQuery);
            userStmt.setString(1, u.getEmail());
            userStmt.setString(2, u.getFirstName());
            userStmt.setString(3, u.getLastName());
            userStmt.setString(4, u.getRole());

            PreparedStatement custStmt = conn.prepareStatement(customerQuery);
            custStmt.setString(1, c.getPhoneNumber());

            if (userStmt.executeUpdate() == 0
                    || custStmt.executeUpdate() == 0) {
                conn.rollback();
                throw new Exception("ERROR: customer was not updated");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return c;
    }

    @Override
    @DELETE
    @Path("{id}")
    public void removeCustomer(@PathParam("id") int id) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String prodOrderQuery = "DELETE FROM product_order\n"
                + "WHERE order_id = "
                + "(SELECT id FROM orders WHERE customer_id = "
                + " (SELECT id FROM customer WHERE user_id = " + id + "))";
            String orderQuery = "DELETE FROM orders\n"
                + "WHERE customer_id = "
                + "(SELECT id FROM customer WHERE user_id = " + id + ")";
            String cAddrQuery = "DELETE FROM customer_address\n"
                + "WHERE customer_id = "
                + "(SELECT id FROM customer WHERE user_id = " + id + ")";
            String custQuery = "DELETE FROM customer WHERE user_id = " + id;
            String passwordQuery = "DELETE FROM password WHERE user_id = " + id;
            String userQuery = "DELETE FROM user WHERE id = " + id;

            PreparedStatement prodOrderStmt = conn.prepareStatement(prodOrderQuery);
            PreparedStatement orderStmt = conn.prepareStatement(orderQuery);
            PreparedStatement passwordStmt = conn.prepareStatement(passwordQuery);
            PreparedStatement cAddrStmt = conn.prepareStatement(cAddrQuery);
            PreparedStatement custStmt = conn.prepareStatement(custQuery);
            PreparedStatement userStmt = conn.prepareStatement(userQuery);

            prodOrderStmt.executeUpdate();
            orderStmt.executeUpdate();

            if (cAddrStmt.executeUpdate() == 0
                    || passwordStmt.executeUpdate() == 0
                    || custStmt.executeUpdate() == 0
                    || userStmt.executeUpdate() == 0) {
                conn.rollback();
                throw new Exception("ERROR: user was not deleted");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
