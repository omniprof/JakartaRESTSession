package com.eclipse.servletclient.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.EntityPart;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is an example of using a Servlet as a client for a web service running
 * in another process in the application server.
 *
 * @author Ken Fogel
 */
@WebServlet(name = "ServletClient", urlPatterns = {"/ServletClient"})
public class ServletClient extends HttpServlet {
    
    private static final Logger LOG = Logger.getLogger(
            ServletClient.class.getName());

    /**
     * Handles the HTTP GET request by calling upon callFileUploadService
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        Response restResponse = callFileUploadService();
        LOG.log(Level.INFO, "\n ]]] doGet restResponse = {0} \n", restResponse.getStatusInfo());
    }

    /**
     * This calls the FileUploadService  running as a separate
     * process on the server or another server.
     *
     * @return Response indication outcome
     * @throws java.io.IOException
     */
    public Response callFileUploadService() throws IllegalStateException, IOException {
        
        // Step 1: Create a Client object
        Client client = ClientBuilder.newClient();

        // Step 2: Create a WebTarget object that points to the service
        WebTarget target = client.target(UriBuilder.fromUri(
                "http://localhost:8080/05_MultiPart_Server/services/multiparts"));

        // Step 3: Create an InputStream for the file to upload
        File initialFile = new File("C:/temp/vwvan1974.jpg");
        InputStream pictureInputStream = new FileInputStream(initialFile);
        
        // Step 4: Create an EntityPart to hold the InputStream
        EntityPart part = EntityPart.withName("part").fileName("vwvan1978.jpg")
                .content(pictureInputStream)
                .mediaType(MediaType.APPLICATION_OCTET_STREAM)
                .build();

        // Step 5: Create an EntityPart to hold the file name
        EntityPart name = EntityPart.withName("name").content("vwvan1974.jpg").build();

        // Step 6: Combine the EntityParts into a GenericEntity
        GenericEntity genericEntity = new GenericEntity<List<EntityPart>>(List.of(name, part)) {};

        // Step 7: Convert the GenericEntity into an Entity
        Entity entity = Entity.entity(genericEntity, MediaType.MULTIPART_FORM_DATA);

        // Step 8:Request the POST FileUploadService with the Enity
        Response response =  target.request(MediaType.MULTIPART_FORM_DATA).post(entity);

        LOG.log(Level.INFO, "\n ]]]callFileUploadService response = {0} \n", response.getStatus());
        
        return response;
    }
}
