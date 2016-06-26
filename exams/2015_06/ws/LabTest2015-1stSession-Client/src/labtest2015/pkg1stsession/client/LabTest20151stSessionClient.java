/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labtest2015.pkg1stsession.client;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author biar
 */
public class LabTest20151stSessionClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //outputs all the names of all clients who have NOT performed any operations in the last days
        List<String> clients = getClients();
        int clientsNum = clients.size() >> 1;
        
        int namePos;
        List<String> operations;
        for(int i = 0; i < clientsNum; i++) {
            operations = getOperationsByClientID(i);
            if(operations.isEmpty()) {
                namePos = (i << 1) + 1;
                System.out.println(clients.get(namePos));
            }
        }
        
        
        //test getOperationDetailsByID
        //System.out.println(getOperationDetaislByID(0));
    }

    private static java.util.List<java.lang.String> getOperationsByClientID(int clientID) {
        server.BankWS_Service service = new server.BankWS_Service();
        server.BankWS port = service.getBankWSPort();
        return port.getOperationsByClientID(clientID);
    }

    private static java.util.List<java.lang.String> getClients() {
        server.AAAWS_Service service = new server.AAAWS_Service();
        server.AAAWS port = service.getAAAWSPort();
        return port.getClients();
    }

    private static java.util.List<java.lang.String> getOperationDetaislByID(int opID) {
        server.BankWS_Service service = new server.BankWS_Service();
        server.BankWS port = service.getBankWSPort();
        return port.getOperationDetaislByID(opID);
    }
    
}
