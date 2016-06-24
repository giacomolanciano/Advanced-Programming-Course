/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

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
public class ProfessorResource {

    private String id;

    /**
     * Creates a new instance of ProfessorResource
     */
    private ProfessorResource(String id) {
        this.id = id;
    }

    /**
     * Get instance of the ProfessorResource
     */
    public static ProfessorResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of ProfessorResource class.
        return new ProfessorResource(id);
    }

    /**
     * Retrieves representation of an instance of server.ProfessorResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getText() {
        String result = ProfessorsResource.myProfessors.get(id);
        return result;
    }

    /**
     * PUT method for updating or creating an instance of ProfessorResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    public void putText(String content) {
        ProfessorsResource.myProfessors.put(id, content);
    }

    /**
     * DELETE method for resource ProfessorResource
     */
    @DELETE
    public void delete() {
        ProfessorsResource.myProfessors.remove(id);
    }
}
