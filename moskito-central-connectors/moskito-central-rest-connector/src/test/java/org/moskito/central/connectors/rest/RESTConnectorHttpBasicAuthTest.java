package org.moskito.central.connectors.rest;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.Test;
import org.moskito.central.Snapshot;
import org.moskito.central.SnapshotMetaData;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.security.Principal;
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
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder("org.moskito.central.endpoints.rest;org.codehaus.jackson.jaxrs")
                .initParam(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, HttpBasicAuthFilter.class.getName()).build();
    }

    @Override
    protected int getPort(int defaultPort) {
        return super.getPort(9988);
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
     * HTTP Basic Authentication request filter for {@link com.sun.jersey.test.framework.spi.container.TestContainer}.
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
        public ContainerRequest filter(ContainerRequest request) {
            User user = authenticate(request);
            request.setSecurityContext(new HttpBasicAuthContext(user));
            return request;
        }

        private User authenticate(ContainerRequest request) {
            // Extract authentication credentials
            String authentication = request.getHeaderValue(ContainerRequest.AUTHORIZATION);
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
            String[] values = new String(Base64.base64Decode(authentication)).split(":");
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
