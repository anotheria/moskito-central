package org.moskito.central.connectors.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.configureme.ConfigurationManager;
import org.moskito.central.Snapshot;
import org.moskito.central.connectors.AbstractCentralConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
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
        WebResource resource = client.resource(getBaseURI());
        resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(snapshot);
    }

    private Client getClient() {
        Client client = Client.create(getClientConfig());
        if (connectorConfig.isBasicAuthEnabled()) {
            /* adding HTTP basic auth header to request */
            client.addFilter(new HTTPBasicAuthFilter(connectorConfig.getLogin(), connectorConfig.getPassword()));
        }

        return client;
    }

    protected ClientConfig getClientConfig() {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getClasses().add(JacksonJaxbJsonProvider.class);
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        return clientConfig;
    }

    protected URI getBaseURI() {
        return UriBuilder.fromUri("http://" + connectorConfig.getHost() + connectorConfig.getResourcePath()).port(connectorConfig.getPort()).build();
    }

    protected RESTConnectorConfig getConnectorConfig(){
        return connectorConfig;
    }

}
