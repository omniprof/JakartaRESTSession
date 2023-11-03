package com.eclipse.restserver;

import com.eclipse.restserver.bean.RestBean;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import java.time.LocalDateTime;

/**
 * This RESTful service takes the "name" you supplied when you called upon the
 * service and adds the date and time along with a String with the name of this
 * service and stores this in a Java Bean.
 * http://localhost:8080/Mod_05_RestServer/services/hello
 *
 * @author Ken Fogel
 */
@Path("hello")
public class GreetingService {

    @Inject
    private RestBean restBean;

    /**
     * If a String is returned then it will be formatted as Text. No need for
     *
     * @Produces({MediaType.APPLICATION_JSON})
     *
     * @param name
     * @return The JSON string to display
     */
    @GET
    public RestBean helloGet(@QueryParam("name") String name) {
        if ((name == null) || name.trim().isEmpty()) {
            name = "Anonymous";
        }
        restBean.setName(name);
        restBean.setTheTime(LocalDateTime.now());
        restBean.setServiceSource("GreetingService");
        return restBean;
    }

    /**
     * When an Object is passed then it is delivered as JSON and turned into the
     * object When an object is returned it is returned as a JSON string.
     *
     * @param restBeanParam
     * @return The JSON string
     */
    @POST
    public RestBean helloPost(RestBean restBeanParam) {
        if ((restBeanParam.getName() == null) || restBeanParam.getName().trim().
                isEmpty()) {
            restBeanParam.setName("Anonymous");
        }
        restBean.setName(restBeanParam.getName());
        restBean.setTheTime(LocalDateTime.now());
        restBean.setServiceSource("GreetingService");
        return restBean;
    }

}
