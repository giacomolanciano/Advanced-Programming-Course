/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myrestclient;


import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:ProfessorsResource [/ps]<br>
 * USAGE:
 * <pre>
        ProfessorsClient client = new ProfessorsClient();
        Object response = client.XXX(...);
        // do whatever with response
        client.close();
 </pre>
 *
 * @author biar
 */
public class ProfessorsClient {

    private WebTarget webTarget;
    private Client client;
    //private static final String BASE_URI = "http://192.168.50.97:8080/SimpleREST/webresources";
    private static final String BASE_URI = "http://localhost:8080/MyRESTServer/webresources";

    public ProfessorsClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        //webTarget = client.target(BASE_URI).path("ps");
        webTarget = client.target(BASE_URI).path("profs");
    }

    public Response postText(Object requestEntity) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.TEXT_PLAIN), Response.class);
    }

    public String getText() throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public void close() {
        client.close();
    }
    
}
