/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.HashMap;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author biar
 */
@Path("/profs")
public class ProfessorsResource {

    @Context
    private UriInfo context;
    
    private Logger LOGGER;
    
    //deve essere static, altrimenti viene resettato ad ogni chiamata del client
    private static int lastPosition = 3;
    public static HashMap<String, String> myProfessors = new HashMap<String, String>() {{
        put("0", "Cip");
        put("1", "Ciop");
        put("2", "Peppuzzo");
    }};

    /**
     * Creates a new instance of ProfessorsResource
     */
    public ProfessorsResource() {
    }

    /**
     * Retrieves representation of an instance of server.ProfessorsResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getText() {
        
        String result = "professors list: ";
        
        for(String x : myProfessors.values()) {
            result += x+", ";
        }
        
        return result;
        
    }

    /**
     * POST method for creating an instance of ProfessorResource
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response postText(String content) {
        myProfessors.put(lastPosition+"", content);
        
        lastPosition += 1;
               
        
        return Response.created(context.getAbsolutePath()).build();
    }

    /**
     * Sub-resource locator method for {id}
     */
    @Path("{id}")
    public ProfessorResource getProfessorResource(@PathParam("id") String id) {
        return ProfessorResource.getInstance(id);
    }
}
