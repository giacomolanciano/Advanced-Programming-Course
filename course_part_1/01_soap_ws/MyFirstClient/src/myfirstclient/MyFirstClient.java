/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myfirstclient;

/**
 *
 * @author biar
 */
public class MyFirstClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.util.List<String> risultato = getElencoUtenti();
        Object[] aux = risultato.toArray();
        for (int i = 0; i < aux.length; i++)
        System.out.println((String)aux[i]);
    }

    private static java.util.List<java.lang.String> getElencoUtenti() {
        it.uniroma1.dis.esempio.ElencoUtentiService_Service service = new it.uniroma1.dis.esempio.ElencoUtentiService_Service();
        it.uniroma1.dis.esempio.ElencoUtentiService port = service.getElencoUtentiServicePort();
        return port.getElencoUtenti();
    }
    
}
