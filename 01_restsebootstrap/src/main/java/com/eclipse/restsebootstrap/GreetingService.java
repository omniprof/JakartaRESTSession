
package com.eclipse.restsebootstrap;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import java.time.LocalDateTime;

/**
 * This class contains the web service methods. You can have multiple request
 * methods of the same type each in its own class or different types of requests
 * in this file.
 *
 * @author Ken Fogel
 */
// This is the path to the service method. It follows the Application path in 
// the URL as in http://localhost:8080/services/hello 
@Path("hello")
public class GreetingService {

    /**
     * The @GET version
     *
     * This method can receive the value from a query string. The annotation
     * {@literal @}QueryParam("name") indicates that the query string is
     * expected to have a "?name=" as in
     * http://localhost:8080/services/hello?name=Moose
     *
     * @param person The name you wish to display in the returned String
     * @return The string to return to the caller
     */
    // Request method type
    @GET
    public String sayCurrentTime(@QueryParam("name") String person) {
        return "01_RestSeBootstrap GET: " + (person == null || person.trim().isEmpty() ? "Anonymous" : person)
                + " - Current date and time is " + LocalDateTime.now();
    }

    /**
     * The @POST version
     *
     * This method can receive the value from a query string. The annotation
     * {@literal @}QueryParam("name") indicates that the query string is
     * expected to have a "?name=" as in
     * curl -i -X POST http://localhost:8080/services/hello?name=Moose
     *
     * @param person The name you wish to display in the returned String
     * @return The string to return to the caller
     */
    // Request method type
    @POST
    public String sayCurrentTimePost(@QueryParam("name") String person) {
        return "01_RestSeBootstrap POST: " + (person == null || person.trim().isEmpty() ? "Anonymous" : person) 
                + " - Current date and time is " + LocalDateTime.now();
    }

}
