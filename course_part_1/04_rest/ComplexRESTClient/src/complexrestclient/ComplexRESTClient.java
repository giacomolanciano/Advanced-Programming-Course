/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complexrestclient;

import dto.Order;
import javax.ws.rs.core.Response;

/**
 *
 * @author biar
 */
public class ComplexRESTClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        JSONClient client = new JSONClient();
        
        Order[] orders = client.getJson(Order[].class);
        
        System.out.println("json: "+orders[0].toString());
        
        String[] items = {"uyfguyf", "ciop", "peppuzzo"};
        Response res = client.postJson(new Order(1, 45, items));
        System.out.println("resp: "+res);
        
        
        
        System.out.println("updated list:");
        orders = client.getJson(Order[].class);
        
        for(Order x :orders) {
            System.out.println(x);
        }
        
        
        
        client.close();
        
    }
  
    
}

    




