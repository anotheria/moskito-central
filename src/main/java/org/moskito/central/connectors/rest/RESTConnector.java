package org.moskito.central.connectors.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
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
 * REST connector implemetation to the Central.
 * 
 * @author dagafonov
 * 
 */
public class RESTConnector extends AbstractCentralConnector {

	/**
	 * Logger instance.
	 */
	private final static Logger log = LoggerFactory.getLogger(RESTConnector.class);

	/**
	 * Connector config instance.
	 */
	private RESTCentralConnectorConfig restConfig;

	private Client getClient() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getClasses().add(JacksonJaxbJsonProvider.class);
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		return client;
	}

	/**
	 * Default constructor.
	 */
	public RESTConnector() {
		super();
	}

	@Override
	public void setConfigurationName(String configurationName) {
		restConfig = new RESTCentralConnectorConfig();
		ConfigurationManager.INSTANCE.configureAs(restConfig, configurationName);
		log.debug("Config: "+restConfig);
	}

	@Override
	protected void sendData(Snapshot snapshot) {
		WebResource resource = getClient().resource(getBaseURI());
		resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(snapshot);
	}

	private URI getBaseURI() {
		return UriBuilder.fromUri("http://" + restConfig.getHost() + restConfig.getResourcePath()).port(restConfig.getPort()).build();
	}

}
