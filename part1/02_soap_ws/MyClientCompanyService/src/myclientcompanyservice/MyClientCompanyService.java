/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myclientcompanyservice;

import companyservice.Company;

/**
 *
 * @author biar
 */
public class MyClientCompanyService {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        System.out.println("id: " + getCompany().getCompanyID() 
                + "\nname: " + getCompany().getCompanyName() 
                + "\nemployees: " + getCompany().getCompanyEmployees());
        
    }

    private static Company getCompany() {
        companyservice.CompanyService_Service service = new companyservice.CompanyService_Service();
        companyservice.CompanyService port = service.getCompanyServicePort();
        return port.getCompany();
    }

    
    
}
