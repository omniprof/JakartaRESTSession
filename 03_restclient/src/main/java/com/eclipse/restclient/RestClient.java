package com.eclipse.restclient;

import com.eclipse.restclient.bean.CompoundBean;
import com.eclipse.restclient.bean.RestBean;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

/**
 * Jakarta web service client
 *
 * @author Ken Fogel
 */
public class RestClient {

    private static final Logger LOG = Logger.getLogger(RestClient.class.
            getName());

    /**
     * This calls the Greeting POST web service running on a server.
     *
     * @param restBean The bean with a name that the service will complete
     * @return A bean that contains the result
     */
    public RestBean callHelloService(RestBean restBean) {

        // Step 0: Create the JSON-B object
        Jsonb jsonb = JsonbBuilder.create();

        //Step 1: Convert restBean into a JSON string
        String restJson = jsonb.toJson(restBean);
        LOG.log(Level.INFO, "\n ]]] Conversion of restBean to JSON= {0} \n", restJson);

        // Step 2: Create a Client object
        Client client = ClientBuilder.newClient();

        // Step 3: Create a WebTarget object that points to the service
        WebTarget target = client.target(UriBuilder.fromUri(
                "http://localhost:8080/03_RestServer/").
                build());

        // Step 4: Pass restJson, the input restBean, to the service and 
        // receive a JSON of the bean with the result added 
        String serviceReturnJson
                = target.path("services").path("hello").request(
                        MediaType.APPLICATION_JSON)
                        .post(Entity.entity(restJson,
                                MediaType.APPLICATION_JSON),
                                String.class);

        LOG.log(Level.INFO, "\n ]]] JSON of result from service call = {0} \n", serviceReturnJson);

        // Step 5: Convert the JSON string into an object
        restBean = jsonb.fromJson(serviceReturnJson, RestBean.class);

        // Display the RestBean after its creation just to confirm that it worked
        LOG.log(Level.INFO, "\n ]]] Conversion of JSON string to restBean = {0} \n", restBean.toString());
        
        return restBean;

    }

    /**
     * This calls the Compound Interest POST web service running on a server.
     *
     * @param compoundBean The bean with the initial data to be used in the
     * calculation
     * @return A bean that contains the result
     */
    public CompoundBean callCompoundService(CompoundBean compoundBean) {

        // Step 0: Create the JSON-B object
        Jsonb jsonb = JsonbBuilder.create();

        //Step 1: Convert compoundBean into a JSON string
        String compoundJson = jsonb.toJson(compoundBean);
        LOG.log(Level.INFO, "\n ]]] Conversion of restBean to JSON= {0} \n", compoundJson);

        // Step 2: Create a Client object
        Client client = ClientBuilder.newClient();

        // Step 3: Create a WebTarget object that points to the service
        WebTarget target = client.target(UriBuilder.fromUri(
                "http://localhost:8080/03_RestServer/").
                build());

        // Step 4: Pass compoundJson, the input compoundBean, to the service and 
        // receive a JSON of the bean with the result added 
        String serviceReturnJson
                = target.path("services").path("compound").request(
                        MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.entity(compoundJson,
                                MediaType.APPLICATION_JSON_TYPE),
                                String.class);

        // Step 5: Convert the JSON string into an object
        compoundBean = jsonb.fromJson(serviceReturnJson, CompoundBean.class);

        // Display the CompoundBean after its creation just to confirm that it worked
        LOG.log(Level.INFO, "\n ]]] Conversion of JSON string to compoundBean = {0} \n", compoundBean.toString());

        return compoundBean;
    }

    /**
     * This is just an example of how little code is required to consume a web
     * service. Doing work in the main method should be avoided.
     *
     * @param args Ignored
     */
    public static void main(String[] args) {

        RestClient restClient = new RestClient();

        RestBean restBean = new RestBean();
        restBean.setName("Ken");
        restBean = restClient.callHelloService(restBean);
        LOG.log(Level.INFO, "\n ]]] restBean in main = {0} \n", restBean.toString());

        restBean = new RestBean();
        restBean.setName("");
        restBean = restClient.callHelloService(restBean);
        LOG.log(Level.INFO, "\n ]]] restBean in main = {0} \n", restBean.toString());

        CompoundBean compoundBean = new CompoundBean(100.00, 0.05, 12.0, 5.0);
        compoundBean = restClient.callCompoundService(compoundBean);
        LOG.log(Level.INFO, "\n ]]] compoundBean in main = {0} \n", compoundBean.toString());

    }
}
