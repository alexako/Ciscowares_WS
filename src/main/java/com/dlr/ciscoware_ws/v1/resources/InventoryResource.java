/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dlr.ciscoware_ws.v1.resources;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author alex
 */
public interface InventoryResource {
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    List<Inventory> getInventory();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    List<Inventory> getProduct(int id);

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    Inventory createInventory(String data);

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    Inventory updateInventory(int id, String data);

    @DELETE
    void removeInventory(int id);
}
