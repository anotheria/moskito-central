package org.moskito.central.endpoints.rmi;

import org.moskito.central.Central;
import org.moskito.central.Snapshot;

/**
 * Central RMI service implementation.
 * 
 * @author dagafonov
 * 
 */
public class RMIEndpointServiceImpl implements RMIEndpointService {

	/**
	 * Instance of local Central.
	 */
	private Central central;

	/**
	 * Default constructor.
	 */
	public RMIEndpointServiceImpl() {
		central = Central.getInstance();
	}

	@Override
	public void processIncomingSnapshot(Snapshot snapshot) throws RMIEndpointServiceException {
		central.processIncomingSnapshot(snapshot);
	}

}
