/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.dis.esempio;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author biar
 */
@WebService(serviceName = "ElencoUtentiService")
public class ElencoUtentiService {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getElencoUtenti")
    public java.lang.String[] getElencoUtenti() {
        // Naive implementation for demonstration purposes
        List<String> utentiAttivi = new ArrayList<String>();
        utentiAttivi.add("Massimo Mecella");
        utentiAttivi.add("Giuseppe De Giacomo");
        utentiAttivi.add("Fabio Patrizi");
        utentiAttivi.add("Maurizio Lenzerini");
        utentiAttivi.add("Domenico Lembo");

        // Logging...
        Logger.getLogger("ActiveUsersWebServiceSample").info("\n"
                + "\t" + "Request for \"getElencoUtentiAttivi() : {String}*\" received." + "\n"
                + "\t" + "Returning " + utentiAttivi.size() + " results...");

        // RIGHT cast: take advantage of
        // 		public <T> T[] toArray(T[] array)
        // defined in ArrayList<T>
        return utentiAttivi.toArray(new String[utentiAttivi.size()]);
        // WRONG code for cast (commented):
        // return (String[])utentiAttivi.toArray();
    }
}
