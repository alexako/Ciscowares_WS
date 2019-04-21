/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dlr.ciscoware_ws.v1.resources.impl;

import com.dlr.ciscoware_ws.v1.resources.Admin;
import com.dlr.ciscoware_ws.v1.resources.AdminResource;
import com.dlr.ciscoware_ws.v1.resources.Branch;
import com.dlr.ciscoware_ws.v1.resources.BranchAddress;
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
@Path("/v1/admins")
public class AdminResourceImpl implements AdminResource {


    String url = "jdbc:mysql://database.alexjreyes.com:3306/java_project";
    String user = "admin";
    String pass = "mapua";

    @Override
    public List<Admin> getAdmins() {

        List<Admin> admins = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT \n" +
                "    a.id,\n" +
                "    u.id,\n" +
                "    u.last_name,\n" +
                "    u.first_name,\n" +
                "    u.email,\n" +
                "    u.role,\n" +
                "    b.id,\n" +
                "    b.name,\n" +
                "    ba.street,\n" +
                "    ba.city,\n" +
                "    ba.province,\n" +
                "    ba.country,\n" +
                "    ba.zip_code\n" +
                "FROM admin a\n" +
                "LEFT JOIN user u\n" +
                "ON a.user_id = u.id\n" +
                "INNER JOIN branch b\n" +
                "ON a.branch_id = b.id\n" +
                "INNER JOIN branch_address ba\n" +
                "ON ba.branch_id = b.id;");

            while (result.next()) {
                User u = new User();
                u.setId(result.getInt(2));
                u.setEmail(result.getString(3));
                u.setFirstName(result.getString(4));
                u.setLastName(result.getString(5));
                u.setRole(result.getString(6));

                BranchAddress ba = new BranchAddress();
                ba.setStreet(result.getString(9));
                ba.setCity(result.getString(10));
                ba.setProvince(result.getString(11));
                ba.setCountry(result.getString(12));
                ba.setZipCode(result.getString(13));

                Branch b = new Branch();
                b.setId(result.getInt(7));
                b.setName(result.getString(8));

                Admin a = new Admin();
                a.setId(result.getInt(1));
                a.setBranchId(b);
                a.setUserId(u);

                admins.add(a);
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
 
        return admins;
    }

    @Override
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Admin getAdmin(@PathParam("id") int id) {

        Admin a = new Admin();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT \n" +
                "    a.id,\n" +
                "    u.id,\n" +
                "    u.last_name,\n" +
                "    u.first_name,\n" +
                "    u.email,\n" +
                "    u.role,\n" +
                "    b.id,\n" +
                "    b.name,\n" +
                "    ba.street,\n" +
                "    ba.city,\n" +
                "    ba.province,\n" +
                "    ba.country,\n" +
                "    ba.zip_code\n" +
                "FROM admin a\n" +
                "LEFT JOIN user u\n" +
                "ON a.user_id = u.id\n" +
                "INNER JOIN branch b\n" +
                "ON a.branch_id = b.id\n" +
                "INNER JOIN branch_address ba\n" +
                "ON ba.branch_id = b.id\n" +
                "WHERE a.id = " + id);

            while (result.next()) {
                User u = new User();
                u.setId(result.getInt(2));
                u.setEmail(result.getString(3));
                u.setFirstName(result.getString(4));
                u.setLastName(result.getString(5));
                u.setRole(result.getString(6));

                BranchAddress ba = new BranchAddress();
                ba.setStreet(result.getString(9));
                ba.setCity(result.getString(10));
                ba.setProvince(result.getString(11));
                ba.setCountry(result.getString(12));
                ba.setZipCode(result.getString(13));

                Branch b = new Branch();
                b.setId(result.getInt(7));
                b.setName(result.getString(8));

                a.setId(result.getInt(1));
                a.setBranchId(b);
                a.setUserId(u);
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
 
        return a;
    }

    @Override
    @POST
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Admin createAdmin(String data) {
        JSONObject obj = new JSONObject(data);
        User u = new User();

        u.setFirstName(obj.getString("firstName"));
        u.setLastName(obj.getString("lastName"));
        u.setEmail(obj.getString("email"));
        u.setRole(obj.getString("role"));

        Branch b = new Branch();
        b.setId(obj.getInt("branchId"));

        Password p = new Password();
        p.setContent(obj.getString("password"));

        Admin a = new Admin();
        a.setUserId(u);
        a.setBranchId(b);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String userQuery = "INSERT INTO user(email, first_name, last_name, role)\n" +
                "VALUES (?, ?, ?, ?);";
            String adminQuery = "INSERT INTO admin(user_id, branch_id)\n" +
                "VALUES (LAST_INSERT_ID(), ?);";
            String passwordQuery = "INSERT INTO password(user_id, content)\n"
                + "VALUES ((SELECT user_id FROM admin WHERE id = LAST_INSERT_ID()), ?);";

            PreparedStatement preparedStmt = conn.prepareStatement(userQuery);
            preparedStmt.setString(1, u.getEmail());
            preparedStmt.setString(2, u.getFirstName());
            preparedStmt.setString(3, u.getLastName());
            preparedStmt.setString(4, u.getRole());

            PreparedStatement passwordStmt = conn.prepareStatement(passwordQuery);
            passwordStmt.setString(1, p.getContent());

            PreparedStatement adminStmt = conn.prepareStatement(adminQuery);
            adminStmt.setString(1, obj.getString("branchId"));

            if (preparedStmt.executeUpdate() == 0
                    || adminStmt.executeUpdate() == 0
                    || passwordStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: user was not created");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return a;
    }

    @Override
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Admin updateAdmin(@PathParam("id") int id, String data) {
        JSONObject obj = new JSONObject(data);
        User u = new User();

        u.setFirstName(obj.getString("firstName"));
        u.setLastName(obj.getString("lastName"));
        u.setEmail(obj.getString("email"));
        u.setRole(obj.getString("role"));

        Branch b = new Branch();
        b.setId(obj.getInt("branchId"));

        Admin a = new Admin();
        a.setUserId(u);
        a.setBranchId(b);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String updateQuery = "UPDATE user SET "
                + "email = ?, "
                + "first_name = ?, "
                + "last_name = ?, "
                + "role = ? "
                + "WHERE id=" + id;
            String adminQuery = "UPDATE admin SET "
                + "branch_id = ?";

            PreparedStatement preparedStmt = conn.prepareStatement(updateQuery);
            preparedStmt.setString(1, u.getEmail());
            preparedStmt.setString(2, u.getFirstName());
            preparedStmt.setString(3, u.getLastName());
            preparedStmt.setString(4, u.getRole());

            PreparedStatement adminStmt = conn.prepareStatement(adminQuery);
            adminStmt.setString(1, a.getBranchId().getId().toString());

            if (preparedStmt.executeUpdate() == 0
                    || adminStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: user was not updated");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return a;
    }

    @Override
    @DELETE
    @Path("{id}")
    public void removeAdmin(@PathParam("id") int id) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String passwordQuery = "DELETE FROM password\n"
                + "WHERE user_id = " + id;
            String deleteQuery = "DELETE FROM admin WHERE user_id=" + id;
            String userQuery = "DELETE FROM user WHERE id = " + id; 

            PreparedStatement passwordStmt = conn.prepareStatement(passwordQuery);
            PreparedStatement preparedStmt = conn.prepareStatement(deleteQuery);
            PreparedStatement userStmt = conn.prepareStatement(userQuery);

            if (passwordStmt.executeUpdate() == 0
                    || preparedStmt.executeUpdate() == 0
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
