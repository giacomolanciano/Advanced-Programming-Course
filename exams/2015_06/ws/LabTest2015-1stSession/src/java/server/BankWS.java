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
import javax.jws.WebParam;

/**
 *
 * @author biar
 */
@WebService(serviceName = "BankWS")
public class BankWS {
    
    /**
     * Naive date implementation to make the computation easier
     */
    private static final int TODAY = 26;
    private static final int INTERVAL = 7;
    
    private static final int OP_ID_POS = 0;
    private static final int CLIENT_ID_POS = 1;
    private static final int DATE_POS = 2;
    
    
       
    /**
     * Web service operation
     */
    @WebMethod(operationName = "getOperationsByClientID")
    public java.lang.String[] getOperationsByClientID(@WebParam(name = "ClientID") int ClientID) {
        
        List<String[]> lastOperations = new ArrayList<String[]>();
        
        //[ID, ID of the performing client, date, amount, description]
        
        lastOperations.add(new String[]{"0", "0", "17", "200", "blah"});
        lastOperations.add(new String[]{"1", "0", "17", "200", "blah"});
        lastOperations.add(new String[]{"2", "0", "22", "200", "Benzina Autostrada"});
        lastOperations.add(new String[]{"3", "0", "24", "200", "blah"});
        lastOperations.add(new String[]{"4", "1", "12", "200", "blah"});
        lastOperations.add(new String[]{"5", "1", "24", "200", "blah"});
        lastOperations.add(new String[]{"6", "1", "12", "200", "blah"});
        lastOperations.add(new String[]{"7", "2", "12", "200", "Benzina Autostrada"});
        lastOperations.add(new String[]{"8", "2", "15", "200", "blah"});
        
    
        List<String> result = new ArrayList<String>();
        int lowerBound = TODAY - INTERVAL;
        
        for(String[] a : lastOperations) {
            
            if(a[CLIENT_ID_POS].equals(ClientID+"")) {
                int date = Integer.parseInt(a[DATE_POS]);
                if(date >= lowerBound && date <= TODAY)
                    result.add(a[OP_ID_POS]);
            }
        }
        
        return result.toArray(new String[result.size()]);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getOperationDetaislByID")
    public java.lang.String[] getOperationDetaislByID(@WebParam(name = "OpID") int OpID) {
        
        List<String[]> lastOperations = new ArrayList<String[]>();
        
        //[ID, ID of the performing client, date, amount, description]
        
        lastOperations.add(new String[]{"0", "0", "17", "200", "blah"});
        lastOperations.add(new String[]{"1", "0", "17", "200", "blah"});
        lastOperations.add(new String[]{"2", "0", "22", "200", "Benzina Autostrada"});
        lastOperations.add(new String[]{"3", "0", "24", "200", "blah"});
        lastOperations.add(new String[]{"4", "1", "12", "200", "blah"});
        lastOperations.add(new String[]{"5", "1", "24", "200", "blah"});
        lastOperations.add(new String[]{"6", "1", "12", "200", "blah"});
        lastOperations.add(new String[]{"7", "2", "12", "200", "Benzina Autostrada"});
        lastOperations.add(new String[]{"8", "2", "15", "200", "blah"});
        
        for(String[] a : lastOperations) {
            
            if(a[OP_ID_POS].equals(OpID+"")) {
                return a;
            }
        }
        return null;
    }
}
