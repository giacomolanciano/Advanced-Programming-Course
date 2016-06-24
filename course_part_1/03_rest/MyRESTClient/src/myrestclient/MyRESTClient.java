/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myrestclient;

/**
 *
 * @author biar
 */
public class MyRESTClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            
            //Test: Get list of all users
            
            ProfessorsClient tester = new ProfessorsClient();
            String professors = tester.getText();
            String result = "SUCCESS";
            if (professors.isEmpty()) {
                result = "FAIL";
            }
            System.out.println("Test case name: testGetAllProfessors, Result: " + result + " >>> " + professors);
        
            //Test: Put a new professor
            String request = "Giacomo";
            javax.ws.rs.core.Response response = tester.postText(request);
            result = "SUCCESS";
            if (response == null) {
                result = "FAIL";
            }
            System.out.println("Test case name: testAddNewProfessor, Result: " + result + " >>> " + response.toString());
            tester.close();
            
            //Test: Get a professor
            ProfessorClient tester2 = new ProfessorClient("7");
            String professor = tester2.getText();
            result = "SUCCESS";
            if (professor.isEmpty()) {
                result = "FAIL";
            }
            System.out.println("Test case name: testProfessorById, Result: " + result + " >>> " + professor);

            //Test: Update a professor
            request = "Davide er triggeratopo";
            tester2.putText(request);
            System.out.println("Test case name: testUpdateProfessorById");

            //Test: Delete a professor
            tester2.delete();
            System.out.println("Test case name: testDeleteProfessorById");
            tester2.close();
            
            

        }
    
}
