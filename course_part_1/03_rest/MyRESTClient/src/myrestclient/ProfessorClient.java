/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myrestclient;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 *
 * @author biar
 */
public class ProfessorClient {

    private WebTarget webTarget;
    private Client client;
    //private static final String BASE_URI = "http://192.168.50.97:8080/SimpleREST/webresources";
    private static final String BASE_URI = "http://localhost:8080/MyRESTServer/webresources";

    public ProfessorClient(String id) {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        //String resourcePath = java.text.MessageFormat.format("ps/{0}", new Object[]{id});
        String resourcePath = java.text.MessageFormat.format("profs/{0}", new Object[]{id});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    public void setResourcePath(String id) {
        //String resourcePath = java.text.MessageFormat.format("ps/{0}", new Object[]{id});
        String resourcePath = java.text.MessageFormat.format("profs/{0}", new Object[]{id});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    public String getText() throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public void putText(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.TEXT_PLAIN));
    }

    public void delete() throws ClientErrorException {
        webTarget.request().delete();
    }

    public void close() {
        client.close();
    }
    
}