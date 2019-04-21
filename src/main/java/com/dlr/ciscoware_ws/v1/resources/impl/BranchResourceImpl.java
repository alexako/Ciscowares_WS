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
import com.dlr.ciscoware_ws.v1.resources.BranchResource;
import com.dlr.ciscoware_ws.v1.resources.Password;
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
@Path("/branches")
public class BranchResourceImpl implements BranchResource {


    String url = "jdbc:mysql://database.alexjreyes.com:3306/java_project";
    String user = "admin";
    String pass = "mapua";

    @Override
    public List<BranchAddress> getBranches() {

        List<BranchAddress> branches = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT\n" +
                "b.id,\n" +
                "b.name,\n" +
                "ba.street, \n" +
                "ba.city, \n" +
                "ba.province,\n" +
                "ba.country,\n" +
                "ba.zip_code\n" +
                "FROM branch b\n" +
                "INNER JOIN branch_address ba\n" +
                "ON ba.branch_id = b.id;"); 

            while (result.next()) {
                Branch b = new Branch();
                b.setId(result.getInt(1));
                b.setName(result.getString(2));

                BranchAddress ba = new BranchAddress();
                ba.setStreet(result.getString(3));
                ba.setCity(result.getString(4));
                ba.setProvince(result.getString(5));
                ba.setCountry(result.getString(6));
                ba.setZipCode(result.getString(7));
                ba.setBranchId(b);

                branches.add(ba);
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
 
        return branches;
    }

    @Override
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public BranchAddress getBranch(@PathParam("id") int id) {

        BranchAddress ba = new BranchAddress();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT\n" +
                "b.id,\n" +
                "b.name,\n" +
                "ba.street, \n" +
                "ba.city, \n" +
                "ba.province,\n" +
                "ba.country,\n" +
                "ba.zip_code\n" +
                "FROM branch b\n" +
                "INNER JOIN branch_address ba\n" +
                "ON ba.branch_id = b.id\n" +
                "WHERE b.id = " + id); 

            while (result.next()) {
                Branch b = new Branch();
                b.setId(result.getInt(1));
                b.setName(result.getString(2));

                ba.setStreet(result.getString(3));
                ba.setCity(result.getString(4));
                ba.setProvince(result.getString(5));
                ba.setCountry(result.getString(6));
                ba.setZipCode(result.getString(7));
                ba.setBranchId(b);
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
 
        return ba;
    }

    @Override
    @POST
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public BranchAddress createBranch(String data) {
        JSONObject obj = new JSONObject(data);
        Branch b = new Branch();
        b.setName(obj.getString("name"));

        BranchAddress ba = new BranchAddress();
        ba.setBranchId(b);
        ba.setStreet(obj.getString("street"));
        ba.setCity(obj.getString("city"));
        ba.setProvince(obj.getString("province"));
        ba.setCountry(obj.getString("country"));
        ba.setZipCode(obj.getString("zipCode"));

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String branchQuery = "INSERT INTO branch(name)\n" +
                "VALUES (?);";
            String branchAddressQuery = "INSERT INTO branch_address("
                + "branch_id, "
                + "street, "
                + "city, "
                + "province, "
                + "zip_code, "
                + "country"
                + ")\n" +
                "VALUES (LAST_INSERT_ID(), ?, ?, ?, ?, ?, ?);";

            PreparedStatement branchStmt = conn.prepareStatement(branchQuery);
            branchStmt.setString(1, b.getName());

            PreparedStatement branchAddressStmt = conn.prepareStatement(branchAddressQuery);
            branchAddressStmt.setString(1, ba.getStreet());
            branchAddressStmt.setString(2, ba.getCity());
            branchAddressStmt.setString(3, ba.getProvince());
            branchAddressStmt.setString(4, ba.getZipCode());
            branchAddressStmt.setString(5, ba.getCountry());

            if (branchStmt.executeUpdate() == 0
                    || branchAddressStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: branch was not created");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return ba;
    }

    @Override
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public BranchAddress updateBranch(@PathParam("id") int id, String data) {
        JSONObject obj = new JSONObject(data);
        Branch b = new Branch();
        b.setName(obj.getString("name"));

        BranchAddress ba = new BranchAddress();
        ba.setBranchId(b);
        ba.setStreet(obj.getString("street"));
        ba.setCity(obj.getString("city"));
        ba.setProvince(obj.getString("province"));
        ba.setCountry(obj.getString("country"));
        ba.setZipCode(obj.getString("zipCode"));

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            String branchAddressQuery = "UPDATE branch_address SET "
                + "branch_id = ?, "
                + "street = ?, "
                + "city = ?, "
                + "province = ?, "
                + "zip_code = ?, "
                + "country = ? "
                + "WHERE id =" + id;
            String branchQuery = "UPDATE branch SET "
                + "name = ?";

            PreparedStatement branchAddressStmt = conn.prepareStatement(branchAddressQuery);
            branchAddressStmt.setString(1, ba.getStreet());
            branchAddressStmt.setString(2, ba.getCity());
            branchAddressStmt.setString(3, ba.getProvince());
            branchAddressStmt.setString(4, ba.getZipCode());
            branchAddressStmt.setString(5, ba.getCountry());

            PreparedStatement branchStmt = conn.prepareStatement(branchQuery);
            branchStmt.setString(1, b.getName());

            if (branchAddressStmt.executeUpdate() == 0
                    || branchStmt.executeUpdate() == 0) {
                throw new Exception("ERROR: branch was not updated");
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return ba;
    }
}
