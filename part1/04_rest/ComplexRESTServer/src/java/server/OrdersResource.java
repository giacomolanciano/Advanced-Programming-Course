/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import dto.Order;
import java.util.ArrayList;
import java.util.HashMap;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author biar
 */
@Path("/orders")
public class OrdersResource {

    @Context
    private UriInfo context;
    
    private static int lastPos = 2;
    private static String[] items = {"cip", "ciop", "peppuzzo"};
    public static final HashMap<String, Order> orders = new HashMap<String, Order>() {{
        put("1", new Order(1, 5567, items));
        put("2", new Order(2, 4, items));
    }};

    /**
     * Creates a new instance of OrdersResource
     */
    public OrdersResource() {
    }

    /**
     * Retrieves representation of an instance of server.OrdersResource
     * @return an instance of dto.Order[]
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public dto.Order[] getJson() {
        
        Order[] result = new Order[orders.size()];
        
        int i = 0;
        for(Order x : orders.values()) {
            result[i] = x;
            i++;
        }
        
        return result;
    }

    /**
     * POST method for creating an instance of OrderResource
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postJson(Order content) {
        
        
        lastPos += 1;
        
        orders.put(lastPos+"", content);
        
        
        
        return Response.created(context.getAbsolutePath()).build();
    }

    /**
     * Sub-resource locator method for {id}
     */
    @Path("{id}")
    public OrderResource getOrderResource(@PathParam("id") String id) {
        return OrderResource.getInstance(id);
    }
}
