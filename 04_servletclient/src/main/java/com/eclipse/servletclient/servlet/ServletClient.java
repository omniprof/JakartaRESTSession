package com.eclipse.servletclient.servlet;

import com.eclipse.servletclient.bean.CompoundBean;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is an example of using a Servlet as a client for a web service running
 * in another application on the server.
 *
 * @author Ken Fogel
 */
@WebServlet(name = "ServletClient", urlPatterns = {"/ServletClient"})
public class ServletClient extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ServletClient.class.
            getName());

    /**
     * Handles the HTTP GET request using the query parameters to construct an
     * object of type CompounbBean that is sent to the Mod_06_RestServer for the
     * calculation. The result is then displayed in a simple web page.
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

        CompoundBean compoundBean = new CompoundBean(
                Double.parseDouble(request.getParameter("principal")),
                Double.parseDouble(request.getParameter("annualInterestRate")),
                Double.parseDouble(request.getParameter("compoundPerTimeUnit")),
                Double.parseDouble(request.getParameter("time")));

        var resultBean = callCompoundService(compoundBean);

        LOG.log(Level.INFO, "doGet compoundBean = {0}", compoundBean.toString());

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            // Using the new TextBlock and formatted String
            out.println("""
            <!DOCTYPE html>
            <html>
                <head>
                    <title>04 ServletJSFClient</title>
                    <link rel='stylesheet' href='styles/main.css' type='text/css'/>
                </head>
                <body>
                    <h2>Principal: %s</h2>
                    <h2>Annual Interest Rate: %s</h2>
                    <h2>Compound Per Time Unit: %s</h2>
                    <h2>Time: %s</h2>
                    <h2>Result: %s</h2>
                </body>
            </html>"""
            .formatted(resultBean.getPrincipal(), 
                       resultBean.getAnnualInterestRate(), 
                       resultBean.getCompoundPerTimeUnit(),
                       resultBean.getTime(),
                       resultBean.getResult()));
        }
    }

    /**
     * This calls the Compound Interest web service running as a separate
     * process on the server or another server.
     *
     * @param compoundBeanLocal is the bean passed from the client
     * @return compoundBean contains the result
     */
    public CompoundBean callCompoundService(CompoundBean compoundBeanLocal) {

        // Step 0: Create the JSON-B object
        Jsonb jsonb = JsonbBuilder.create();

        //Step 1: Convert compoundBean into a JSON string
        String restJson = jsonb.toJson(compoundBeanLocal);
        LOG.log(Level.INFO, "\n]]] JSON restBean = {0} \n", restJson);

        // Step 2: Create a Client object
        Client client = ClientBuilder.newClient();

        // Step 3: Create a WebTarget object that points to the service
        WebTarget target = client.target(UriBuilder.fromUri(
                "http://localhost:8080/04_RestServer/").
                build());

        // Step 4: Pass restJson, the input compoundBean, to the service and 
        // receive a JSON of the bean with the resultBean added 
        String serviceReturnJson
                = target.path("services").path("compound").request(
                        MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.entity(restJson,
                                MediaType.APPLICATION_JSON_TYPE),
                                String.class);

        // Step 5: Convert the JSON string into an object
        CompoundBean compoundBean = jsonb.
                fromJson(serviceReturnJson, CompoundBean.class);

        // Display the CompoundBean after its creation just to confirm that it worked
        LOG.log(Level.INFO, "\n ]]]compoundBean = {0} \n", compoundBean.toString());
        return compoundBean;
    }
}
