/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dlr.ciscoware_ws.v1.resources.impl;

import com.dlr.ciscoware_ws.v1.resources.Password;
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
@Path("/users")
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
            ResultSet result = stmt.executeQuery("SELECT\n" +
                "	u.id,\n" +
                "	u.email,\n" +
                "	u.first_name,\n" +
                "	u.last_name,\n" +
                "	u.role,\n" +
                "	p.content\n" +
                "FROM user u\n" +
                "INNER JOIN password p\n" +
                "ON u.id = p.user_id;");

            while (result.next()) {
                Password p = new Password();
                p.setContent(result.getString(6));
                List<Password> cp = new ArrayList<>();
                cp.add(p);

                User u = new User();
                u.setId(result.getInt(1));
                u.setEmail(result.getString(2));
                u.setFirstName(result.getString(3));
                u.setLastName(result.getString(4));
                u.setRole(result.getString(5));
                u.setPasswordCollection(cp);
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
            ResultSet result = stmt.executeQuery("SELECT\n" +
                "	u.id,\n" +
                "	u.email,\n" +
                "	u.first_name,\n" +
                "	u.last_name,\n" +
                "	u.role,\n" +
                "	p.content\n" +
                "FROM user u\n" +
                "INNER JOIN password p\n" +
                "ON u.id = p.user_id" +
                "WHERE u.id = " + id);

            while (result.next()) {
                Password p = new Password();
                p.setContent(result.getString(6));
                List<Password> cp = new ArrayList<>();
                cp.add(p);

                u.setId(result.getInt(1));
                u.setEmail(result.getString(2));
                u.setFirstName(result.getString(3));
                u.setLastName(result.getString(4));
                u.setRole(result.getString(5));
                u.setPasswordCollection(cp);
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

    @Override
    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String login(String data) {

        JSONObject obj = new JSONObject(data);
        JSONObject resp = new JSONObject();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT\n" +
                "	u.id,\n" +
                "	u.email,\n" +
                "	u.first_name,\n" +
                "	u.last_name,\n" +
                "	u.role,\n" +
                "	p.content\n" +
                "FROM user u\n" +
                "INNER JOIN password p\n" +
                "ON u.id = p.user_id;");

            while (result.next()) {

                User u = new User();
                u.setId(result.getInt(1));
                u.setEmail(result.getString(2));
                u.setFirstName(result.getString(3));
                u.setLastName(result.getString(4));
                u.setRole(result.getString(5));

                Password p = new Password();
                p.setContent(result.getString(6));

                JSONObject uObj = new JSONObject();
                uObj.put("id", u.getId());
                uObj.put("email", u.getEmail());
                uObj.put("firstName", u.getFirstName());
                uObj.put("lastName", u.getLastName());
                uObj.put("role", u.getRole());

                if (u.getEmail().equals(obj.getString("email"))
                        && p.getContent().equals(obj.getString("password"))) {
                    resp.put("code", "200");
                    resp.put("message", "Access granted");
                    resp.put("user", uObj);
                    return resp.toString();
                }
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        resp.put("code", "401");
        resp.put("message", "Access denied");
        return resp.toString();
    }

    @Override
    @POST
    @Path("change-password")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String changePassword(String data) {

        JSONObject obj = new JSONObject(data);

        if (!this.valid(data)) {
            return "{ \"code\": \"401\" }";
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String updateQuery = "UPDATE password p\n" +
                "INNER JOIN user u\n" +
                "ON p.user_id = u.id\n" +
                "SET p.content = ?" +
                "WHERE u.email = ?" ;

            PreparedStatement preparedStmt = conn.prepareStatement(updateQuery);
            preparedStmt.setString(1, obj.getString("newPassword"));
            preparedStmt.setString(2, obj.getString("email"));

            if (preparedStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: password was not updated");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return data;
    }

    private boolean valid(String data) {
        JSONObject obj = new JSONObject(data);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT\n" +
                "	u.email,\n" +
                "	p.content\n" +
                "FROM user u\n" +
                "INNER JOIN password p\n" +
                "ON u.id = p.user_id;");

            while (result.next()) {
                if (result.getString(1).equals(obj.getString("email"))
                        && result.getString(2).equals(obj.getString("password"))) {
                    return true;
                }
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }
}
