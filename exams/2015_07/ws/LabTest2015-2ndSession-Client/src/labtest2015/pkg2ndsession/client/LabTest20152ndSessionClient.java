/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labtest2015.pkg2ndsession.client;

import java.util.List;

/**
 *
 * @author biar
 */
public class LabTest20152ndSessionClient {

    private static final int DESCR_POS = 4;
    private static final String TAG = "Benzina Autostrada";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //outputs  all the names of all clients who have performed an operation in the last days with description “Benzina autostrada”
        List<String> clients = getClients();
        int clientsNum = clients.size() >> 1;
        
        int namePos;
        List<String> operations;
        List<String> opDetails;
        for(int i = 0; i < clientsNum; i++) {
            operations = getOperationsByClientID(i);
            if(!operations.isEmpty()) {
                for(String o : operations){
                    opDetails = getOperationDetaislByID(Integer.parseInt(o));
                    if(opDetails.get(DESCR_POS).equals(TAG)) {
                        namePos = (i << 1) + 1;
                        System.out.println(clients.get(namePos));
                    }
                }
            }
        }
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

    private static java.util.List<java.lang.String> getOperationsByClientID(int clientID) {
        server.BankWS_Service service = new server.BankWS_Service();
        server.BankWS port = service.getBankWSPort();
        return port.getOperationsByClientID(clientID);
    }
    
}
