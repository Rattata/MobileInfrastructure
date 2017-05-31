package com.hva.server;

import com.hva.server.infrastructure.ConnectionFactory;
import com.hva.server.infrastructure.ResourceBinder;
import com.hva.server.infrastructure.SpotifyServiceFactory;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;

import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Main class.
 *
 */
public class Main {

    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/myapp/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
     * application.
     *
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        final ResourceConfig rc = GetResourceConfig();

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static ResourceConfig GetResourceConfig() {
        return new ResourceConfig()
                .register(new ConnectionFactory())
                .register(new ResourceBinder())
                .packages("com.hva.server")
                .packages("com.hva.server.resources");
    }

    /**
     * Main method.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }
}