/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;

/**
 *
 * @author biar
 */
@WebService(serviceName = "AAAWS")
public class AAAWS {


    /**
     * Web service operation
     */
    @WebMethod(operationName = "getClients")
    public java.lang.String[] getClients() {
        List<String> clients = new ArrayList<String>();
        
        clients.add("0");
        clients.add("Massimo Mecella");
        clients.add("1");
        clients.add("Camil Demetrescu");
        clients.add("2");
        clients.add("Maurizio Lenzerini");
       
        return clients.toArray(new String[clients.size()]);
    }
}
