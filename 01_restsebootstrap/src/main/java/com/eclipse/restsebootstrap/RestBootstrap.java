package com.eclipse.restsebootstrap;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.SeBootstrap;
import java.io.IOException;
import jakarta.ws.rs.core.Application;
import java.util.Set;
import java.util.logging.Logger;

/**
 * This class configures and starts the SeBootstrap. This is the embedded
 * server. It uses the class that extended Application, RestConfig, to define
 * the classes that contain web service methods.
 *
 * @author Ken Fogel
 */
@ApplicationPath("services")
public class RestBootstrap extends Application {
    
    private static final Logger LOG = Logger.getLogger(RestBootstrap.class.
            getName());

    @Override
    public Set<Class<?>> getClasses() {
        return Set.of(GreetingService.class, CompoundInterestService.class);
    }

    public void startService() throws IOException {
        SeBootstrap.Configuration configuration = SeBootstrap.Configuration.
                builder()
                .host("localhost")
                .port(8080)
                .protocol("http")
                .build();

        SeBootstrap.start(this, configuration);

        // Used when the service must run until you kill the process
        // Thread.currentThread().join();

        // Used when you want to end the service by pressing Enter
        System.out.println("Press Enter to end this process"); 
        System.in.read();
    }

    public static void main(final String[] args) throws InterruptedException, IOException {
        new RestBootstrap().startService();
        System.exit(0);
    }
}
