package org.moskito.central.connectors.rest;

import jakarta.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.moskito.central.Snapshot;
import org.moskito.central.SnapshotMetaData;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.util.HashMap;

/**
 * Test class for MoSKito Central HTTPS REST-connector.
 *
 * @author Vladyslav Bezuhlyi
 */
@Ignore
public class RESTConnectorHttpsTest {

    /**
     * Connector instance under test.
     */
    private ExposedRESTHttpsConnector connector;


    /**
     * Exposing send method to test the HTTPS REST-connector.
     */
    public static class ExposedRESTHttpsConnector extends RESTHttpsConnector {

        public ExposedRESTHttpsConnector() {
            super.setConfigurationName("rest-connector-https");
        }

        public RESTConnectorConfig getConfig() {
            return getConnectorConfig();
        }

        @Override
        public void sendData(Snapshot snapshot) {
            super.sendData(snapshot);
        }

    }

    /**
     * HTTPS-enabled Server for tests.
     */
    public static class TestServer {

        private static HttpServer server;


        private static URI getBaseURI() {
            return UriBuilder.fromUri("https://localhost/").port(9463).build();
        }

        public static void start() {
            WebappContext webappContext = new WebappContext("TestContext");
            ServletRegistration registration = webappContext.addServlet("ServletContainer", ServletContainer.class);
            registration.setInitParameter("jersey.config.server.provider.packages", "org.moskito.central.connectors.rest;org.codehaus.jackson.jaxrs");
            registration.addMapping("/*");

            SSLContextConfigurator sslConfigurator = new SSLContextConfigurator();
            sslConfigurator.setKeyStoreFile("./target/test-classes/central_server_keystore.jks");
            sslConfigurator.setKeyStorePass("moskito");
            SSLContext sslContext = sslConfigurator.createSSLContext();

            try {
                server = GrizzlyHttpServerFactory.createHttpServer(
                        getBaseURI(),
                        null,
                        true,
                        new SSLEngineConfigurator(sslContext).setClientMode(false).setNeedClientAuth(false)
                );

                webappContext.deploy(server);
                server.start();
            } catch (Exception e) {
                System.out.println("Error while starting the test server: " + e);
            }
        }

        public static void stop() {
            server.stop();
        }

    }


    @Before
    public void startServer() {
        TestServer.start();
        /* Each time the new connector with configuration as per config file */
        connector = new ExposedRESTHttpsConnector();
    }

    @After
    public void stopServer() {
        TestServer.stop();
    }


    @Test
    public void testHttpsWithTrustStore() {
        /**
         * Here and further we assume that start config for tests in rest-connector-https.json is the next:
         * - correct truststore and password is specified,
         * - host verification disabled,
         * - untrusted (self-signed) certificates acceptance disabled
         *
         * And the server certificate used in test contains CN != "localhost" field.
         **/
        addSnapshot();
    }

    @Test(expected = Exception.class)
    public void testHostVerification() {
        connector.getConfig().setHostVerificationEnabled(true);
        addSnapshot();
    }

    @Test
    public void testSelfSignedWithoutTrustStore() {
        connector.getConfig().setTrustStoreFilePath(null);
        connector.getConfig().setTrustSelfSigned(true);
        addSnapshot();
    }

    @Test(expected = Exception.class)
    public void testSelfSignedWithHostVerification() {
        connector.getConfig().setTrustStoreFilePath(null);
        connector.getConfig().setTrustSelfSigned(true);
        connector.getConfig().setHostVerificationEnabled(true);
        addSnapshot();
    }


    public void addSnapshot() {
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

        connector.sendData(snapshot);
    }

}
