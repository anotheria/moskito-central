package org.moskito.central.connectors.rest;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.moskito.central.Snapshot;
import org.moskito.central.SnapshotMetaData;

import javax.net.ssl.SSLContext;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.HashMap;

/**
 * Test class for MoSKito Central HTTPS REST-connector.
 *
 * @author Vladyslav Bezuhlyi
 */
public class RESTConnectorHttpsTest {

    /**
     * Exposing send method to test the HTTPS REST-connector.
     */
    public static class ExposedRESTHttpsConnector extends RESTHttpsConnector {

        public ExposedRESTHttpsConnector() {
            super.setConfigurationName("rest-connector-https");
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
            registration.setInitParameter(PackagesResourceConfig.PROPERTY_PACKAGES, "org.moskito.central.connectors.rest;org.codehaus.jackson.jaxrs");
            registration.addMapping("/*");

            SSLContextConfigurator sslConfigurator = new SSLContextConfigurator();
            sslConfigurator.setKeyStoreFile("./keystore_server");
            sslConfigurator.setKeyStorePass("asdfgh");
            SSLContext sslContext = sslConfigurator.createSSLContext();

            try {
                server = GrizzlyServerFactory.createHttpServer(
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
    }

    @After
    public void stopServer() {
        TestServer.stop();
    }

    @Test
    public void testAddSnapshot() throws InterruptedException {
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

        ExposedRESTHttpsConnector connector = new ExposedRESTHttpsConnector();
        connector.sendData(snapshot);
        Thread.sleep(1000);
    }

}
