package org.moskito.central.connectors.rest;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.moskito.central.Snapshot;
import org.moskito.central.SnapshotMetaData;

import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;

/**
 * Test class for MoSKito Central REST-connector with HTTP basic authentication enabled.
 *
 * @author Vladyslav Bezuhlyi
 */
public class RESTConnectorHttpBasicAuthTest extends JerseyTest {

    /**
     * Exposing send method to test the connector.
     */
    public static class ExposedRESTConnector extends RESTConnector {

        public ExposedRESTConnector() {
            super.setConfigurationName("rest-connector-auth");
        }

        @Override
        public void sendData(Snapshot snapshot) {
            super.sendData(snapshot);
        }

    }


    @Override
    protected Application configure() {
        return new ResourceConfig().packages("org.moskito.central.connectors.rest;org.codehaus.jackson.jaxrs").property("jakarta.ws.rs.container.ContainerRequestFilter", HttpBasicAuthFilter.class.getName());
    }

    @Test
    public void testHttpBasicAuth() throws InterruptedException {
        ExposedRESTConnector connector = new ExposedRESTConnector();
        connector.sendData(prepareSnapshot());
        Thread.sleep(1000);
    }

    public Snapshot prepareSnapshot() {
        Snapshot snapshot = new Snapshot();

        SnapshotMetaData metaData = new SnapshotMetaData();
        metaData.setProducerId("prodId");
        metaData.setCategory("catId");
        metaData.setSubsystem("subSId");

        snapshot.setMetaData(metaData);

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("firstname", "moskito");
        data.put("lastname", "central");
        snapshot.addSnapshotData("test", data);
        snapshot.addSnapshotData("test2", data);
        snapshot.addSnapshotData("test3", data);

        return snapshot;
    }

    /**
     * HTTP Basic Authentication request filter for {@link org.glassfish.jersey.test.spi.TestContainer}.
     * Simulates enabled HTTP basic authentication on server's side.
     */
    public static class HttpBasicAuthFilter implements ContainerRequestFilter {

        /**
         * User login (and role). Should be the same as in rest-connector-auth.json
         */
        private final String user = "testlogin";

        /**
         * User password. Should be the same as in rest-connector-auth.json
         */
        private final String password = "testpassword";

        /**
         * Injectable URI info.
         */
        @Context
        UriInfo uriInfo;


        @Override
        public void filter(ContainerRequestContext request) {
            User user = authenticate(request);
            request.setSecurityContext(new HttpBasicAuthContext(user));
        }

        private User authenticate(ContainerRequestContext request) {
            // Extract authentication credentials
            String authentication = request.getHeaderString(ContainerRequest.AUTHORIZATION);
            if (authentication == null) {
                throw new WebApplicationException(401);
                // Authentication credentials are required
            }
            if (!authentication.startsWith("Basic ")) {
                throw new WebApplicationException(406);
                // Additional checks should be done here
                // Only HTTP Basic authentication is supported
            }
            authentication = authentication.substring("Basic ".length());
            String[] values = new String(Base64.getDecoder().decode(authentication)).split(":");
            if (values.length < 2) {
                throw new WebApplicationException(401);
                // Invalid syntax for username and password
            }
            String username = values[0];
            String password = values[1];
            if ((username == null) || (password == null)) {
                throw new WebApplicationException(401);
                // Missing username or password
            }

            // Validate the extracted credentials
            User user = null;
            if (username.equals(this.user) && password.equals(this.password)) {
                user = new User(this.user, this.user);
            } else {
                throw new WebApplicationException(406);
                // Invalid username or password
            }
            return user;
        }


        public class HttpBasicAuthContext implements SecurityContext {

            private User user;
            private Principal principal;

            public HttpBasicAuthContext(final User user) {
                this.user = user;
                this.principal = new Principal() {

                    public String getName() {
                        return user.username;
                    }
                };
            }

            public Principal getUserPrincipal() {
                return this.principal;
            }

            public boolean isUserInRole(String role) {
                return (role.equals(user.role));
            }

            public boolean isSecure() {
                return "https".equals(uriInfo.getRequestUri().getScheme());
            }

            public String getAuthenticationScheme() {
                return SecurityContext.BASIC_AUTH;
            }
        }


        public class User {

            public String username;
            public String role;

            public User(String username, String role) {
                this.username = username;
                this.role = role;
            }
        }

    }

}
