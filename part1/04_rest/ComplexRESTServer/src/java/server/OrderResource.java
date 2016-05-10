/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import dto.Order;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author biar
 */
public class OrderResource {

    private String id;

    /**
     * Creates a new instance of OrderResource
     */
    private OrderResource(String id) {
        this.id = id;
    }

    /**
     * Get instance of the OrderResource
     */
    public static OrderResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of OrderResource class.
        return new OrderResource(id);
    }

    /**
     * Retrieves representation of an instance of server.OrderResource
     * @return an instance of dto.Order
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Order getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of OrderResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(Order content) {
    }

    /**
     * DELETE method for resource OrderResource
     */
    @DELETE
    public void delete() {
    }
}
