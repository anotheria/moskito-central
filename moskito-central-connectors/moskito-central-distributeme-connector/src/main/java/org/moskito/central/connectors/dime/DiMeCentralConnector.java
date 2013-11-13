package org.moskito.central.connectors.dime;

import net.anotheria.util.IdCodeGenerator;
import org.configureme.ConfigurationManager;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.core.ServiceDescriptor.Protocol;
import org.moskito.central.Snapshot;
import org.moskito.central.connectors.AbstractCentralConnector;
import org.moskito.central.endpoints.rmi.RMIEndpointService;
import org.moskito.central.endpoints.rmi.RMIEndpointServiceException;
import org.moskito.central.endpoints.rmi.generated.RemoteRMIEndpointServiceStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Moskito connector for RMI processing of incoming snapshots.
 * 
 * @author dagafonov
 * 
 */
public class DiMeCentralConnector extends AbstractCentralConnector {

	/**
	 * Logger instance.
	 */
	private final static Logger log = LoggerFactory.getLogger(DiMeCentralConnector.class);

	/**
	 * {@link RMIEndpointService} service instance.
	 */
	private RMIEndpointService centralService;

	/**
	 * 
	 */
	private DiMeCentralConnectorConfig config;

	/**
	 * Default constructor.
	 */
	public DiMeCentralConnector() {
		super();
	}

	@Override
	public void setConfigurationName(String configurationName) {
		config = new DiMeCentralConnectorConfig();
		ConfigurationManager.INSTANCE.configureAs(config, configurationName);
		log.debug("Config: "+config);
		
		Protocol aProtocol = Protocol.RMI;
		String aServiceId = RMIEndpointService.class.getName().replaceAll("[.]", "_");
		String anInstanceId = IdCodeGenerator.generateCode(10);
		String aHost = config.getConnectorHost();
		int aPort = config.getConnectorPort();
		ServiceDescriptor remote = new ServiceDescriptor(aProtocol, aServiceId, anInstanceId, aHost, aPort);

		centralService = new RemoteRMIEndpointServiceStub(remote);
	}

	@Override
	protected void sendData(Snapshot snapshot) {
		if (centralService != null) {
			try {
				centralService.processIncomingSnapshot(snapshot);
			} catch (RMIEndpointServiceException e) {
				throw new RuntimeException("centralService.processIncomingSnapshot failed...", e);
			}
		}
	}

}
