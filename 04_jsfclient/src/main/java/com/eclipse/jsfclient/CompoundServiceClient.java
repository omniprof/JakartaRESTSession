package com.eclipse.jsfclient;

import com.eclipse.jsfclient.bean.CompoundBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains the calculations. The Named annotation allows CDI to make
 * this service available on the Faces page.
 *
 * @author Ken Fogel
 */
@Named
@RequestScoped
public class CompoundServiceClient {

    private static final Logger LOG = Logger.getLogger(
            CompoundServiceClient.class.getName());
    /*
     * The curremt instance of the data bean managed by CDI. It will contain 
     * the data enetered into the form
     */
    @Inject
    private CompoundBean compoundBean;

    /**
     * This calls the Compound Interest web service running as a separate
     * process on the server or another server.
     *
     * Faces requires that action methods called from a Faces page must return a
     * String. A return of null is acceptable if whatever is returned is
     * ignored.
     *
     * @param compoundBeanLocal
     * @return Faces actions must return a String, null implies that whatever is
     * returned, even null, is not used.
     */
    public String callCompoundService(CompoundBean compoundBeanLocal) {

        // Step 0: Create the JSON-B object
        Jsonb jsonb = JsonbBuilder.create();

        //Step 1: Convert compoundBean into a JSON string
        String restJson = jsonb.toJson(compoundBeanLocal);
        LOG.log(Level.INFO, "JSON restBean = {0}", restJson);

        // Step 2: Create a Client object
        Client client = ClientBuilder.newClient();

        // Step 3: Create a WebTarget object that points to the service
        WebTarget target = client.target(UriBuilder.fromUri(
                "http://localhost:8080/04_RestServer/").
                build());

        // Step 4: Pass restJson, the input compoundBean, to the service and 
        // receive a JSON of the bean with the result added 
        String serviceReturnJson
                = target.path("services").path("compound").request(
                        MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.entity(restJson,
                                MediaType.APPLICATION_JSON_TYPE),
                                String.class);

        // Step 5: Convert the JSON string into an object
        compoundBeanLocal = jsonb.
                fromJson(serviceReturnJson, CompoundBean.class);

        // Step 6 for JSF: Transfer the result from the local bean to the CDI bean
        compoundBean.setResult(compoundBeanLocal.getResult());

        // Display the CompoundBean after its creation just to confirm that it worked
        LOG.log(Level.INFO, "compoundBean = {0}", compoundBean.toString());
        return null;
    }
}
