package com.eclipse.restclient;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import java.util.logging.Logger;
import jakarta.ws.rs.core.EntityPart;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;

/**
 * Jakarta web service client
 *
 * @author Ken Fogel
 */
public class RestClient {

    private static final Logger LOG = Logger.getLogger(
            RestClient.class.getName());

    /**
     * This calls the FileUpload POST web service running on a server.
     *
     * @param fileName
     * @param path
     * @return A bean that contains the result
     * @throws java.io.IOException
     */
    public Response callFileUploadService(String fileName, String path) throws IllegalStateException, IOException {

        // Step 1: Create a Client object
        Client client = ClientBuilder.newClient();

        // Step 2: Create a WebTarget object that points to the service
        WebTarget target = client.target(UriBuilder.fromUri(
                "http://localhost:8080/05_MultiPart_Server/services/multiparts"));

        // Step 3: Create an InputStream for the file to upload
        File initialFile = new File(path + fileName);
        InputStream pictureInputStream = new FileInputStream(initialFile);
        
        // Step 4: Create an EntityPart to hold the InputStream
        EntityPart part = EntityPart.withName("part").fileName(fileName)
                .content(pictureInputStream)
                .mediaType(MediaType.APPLICATION_OCTET_STREAM)
                .build();
        
        // Step 5: Create an EntityPart to hold the file name
        EntityPart name = EntityPart.withName("name").content(fileName).build();
        
        // Step 6: Combine the EntityParts into a GenericEntity
        GenericEntity genericEntity = new GenericEntity<List<EntityPart>>(List.of(name, part)) {};
        
        // Step 7: Convert the GenericEntity into an Entity
        Entity entity = Entity.entity(genericEntity, MediaType.MULTIPART_FORM_DATA);
        
        // Step 8:Request the POST FileUploadService with the Enity
        Response response = target.request(MediaType.MULTIPART_FORM_DATA).post(entity);
        
        LOG.log(Level.INFO, "callFileUploadService response = {0}", response.getStatus());

        return response;
    }     

    /**
     * This is just an example of how little code is required to consume a web
     * service. Doing work in the main method should be avoided.
     *
     * @param args Ignored
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IllegalStateException, IOException {

        RestClient restClient = new RestClient();
        restClient.callFileUploadService("vwvan1974.jpg", "C:/temp/");
    }
}
