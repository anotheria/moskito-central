package org.moskito.central.connectors.rest;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import org.configureme.ConfigurationManager;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.moskito.central.Snapshot;
import org.moskito.central.connectors.AbstractCentralConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * REST connector implementation to the Central.
 *
 * @author dagafonov
 */
public class RESTConnector extends AbstractCentralConnector {

	/**
	 * Logger instance.
	 */
	private final static Logger log = LoggerFactory.getLogger(RESTConnector.class);

	/**
	 * Connector config instance.
	 */
	private RESTConnectorConfig connectorConfig;

    /**
     * Cached client instance.
     */
    private volatile Client client;

	/**
	 * Default constructor.
	 */
	public RESTConnector() {
		super();
	}


    @Override
    public void setConfigurationName(String configurationName) {
        connectorConfig = new RESTConnectorConfig();
        ConfigurationManager.INSTANCE.configureAs(connectorConfig, configurationName);
        super.configure(connectorConfig);

        log.debug("Config: " + connectorConfig);
        client = getClient();
    }

    @Override
    protected void sendData(Snapshot snapshot) {
        WebTarget webTarget = client.target(getBaseURI());
        webTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(snapshot));
    }

    protected Client getClient() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(JacksonFeature.class);

        if (connectorConfig.isBasicAuthEnabled()) {
            HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder()
                    .credentials(connectorConfig.getLogin(), connectorConfig.getPassword())
                    .build();
            clientConfig.register(feature);
        }

        return ClientBuilder.newClient(clientConfig);
    }

    protected ClientConfig getClientConfig() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(JacksonFeature.class);
        return clientConfig;
    }

    protected URI getBaseURI() {
        return UriBuilder.fromUri("http://" + connectorConfig.getHost() + connectorConfig.getResourcePath()).port(connectorConfig.getPort()).build();
    }

    protected RESTConnectorConfig getConnectorConfig(){
        return connectorConfig;
    }

}
