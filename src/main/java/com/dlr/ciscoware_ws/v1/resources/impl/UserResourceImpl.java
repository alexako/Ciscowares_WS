/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dlr.ciscoware_ws.v1.resources.impl;

import com.dlr.ciscoware_ws.v1.resources.User;
import com.dlr.ciscoware_ws.v1.resources.UserResource;
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
@Path("/v1/users")
public class UserResourceImpl implements UserResource {


    String url = "jdbc:mysql://database.alexjreyes.com:3306/java_project";
    String user = "admin";
    String pass = "mapua";

    @Override
    public List<User> getUsers() {

        List<User> users = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM user");

            while (result.next()) {
                User u = new User();
                u.setId(result.getInt(1));
                u.setEmail(result.getString(2));
                u.setFirstName(result.getString(3));
                u.setLastName(result.getString(4));
                u.setRole(result.getString(5));
                users.add(u);
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
 
        return users;
    }

    @Override
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public User getUser(@PathParam("id") int id) {
        
        User u = new User();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM user u WHERE u.id = " + id);

            while (result.next()) {
                u.setId(result.getInt(1));
                u.setEmail(result.getString(2));
                u.setFirstName(result.getString(3));
                u.setLastName(result.getString(4));
                u.setRole(result.getString(5));
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

 
        return u;
    }

    @Override
    @POST
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public User createUser(String data) {
        JSONObject obj = new JSONObject(data);
        User u = new User();

        u.setFirstName(obj.getString("firstName"));
        u.setLastName(obj.getString("lastName"));
        u.setEmail(obj.getString("email"));
        u.setRole(obj.getString("role"));

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String insertQuery = "INSERT INTO user(email, first_name, last_name, role)\n" +
                "VALUES (?, ?, ?, ?);";

            PreparedStatement preparedStmt = conn.prepareStatement(insertQuery);
            preparedStmt.setString(1, u.getEmail());
            preparedStmt.setString(2, u.getFirstName());
            preparedStmt.setString(3, u.getLastName());
            preparedStmt.setString(4, u.getRole());

            if (preparedStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: user was not created");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return u;
    }

    @Override
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public User updateUser(@PathParam("id") int id, String data) {
        JSONObject obj = new JSONObject(data);
        User u = new User();

        u.setFirstName(obj.getString("firstName"));
        u.setLastName(obj.getString("lastName"));
        u.setEmail(obj.getString("email"));
        u.setRole(obj.getString("role"));

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String updateQuery = "UPDATE user SET "
                + "email = ?, "
                + "first_name = ?, "
                + "last_name = ?, "
                + "role = ? "
                + "WHERE id=" + id;

            PreparedStatement preparedStmt = conn.prepareStatement(updateQuery);
            preparedStmt.setString(1, u.getEmail());
            preparedStmt.setString(2, u.getFirstName());
            preparedStmt.setString(3, u.getLastName());
            preparedStmt.setString(4, u.getRole());

            if (preparedStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: user was not updated");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return u;
    }

    @Override
    @DELETE
    @Path("{id}")
    public void removeUser(@PathParam("id") int id) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String deleteQuery = "DELETE FROM user WHERE id=" + id;

            PreparedStatement preparedStmt = conn.prepareStatement(deleteQuery);

            if (preparedStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: user was not deleted");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
