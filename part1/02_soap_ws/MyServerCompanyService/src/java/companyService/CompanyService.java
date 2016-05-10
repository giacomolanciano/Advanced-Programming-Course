/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package companyService;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author biar
 */
@WebService(serviceName = "CompanyService")
public class CompanyService {

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
    @WebMethod(operationName = "getCompany")
    public Company getCompany() {
        String[] employees = {"giacomo", "luca", "davide"};
        Company result = new Company();
        result.companyEmployees = employees;
        result.companyID = "1111";
        result.companyName =  "TakeATrip";
        return result;
    }
}
