package org.moskito.central.connectors.embedded;

import org.moskito.central.Central;
import org.moskito.central.Snapshot;
import org.moskito.central.connectors.AbstractCentralConnector;

/**
 * This connector allows to run moskito central embedded in any moskito
 * instance. This is useful for 1 server systems to reduce complexity.
 * 
 * @author lrosenberg
 * @author dagafonov
 * @since 20.03.13 14:17
 */
public class EmbeddedConnector extends AbstractCentralConnector {

	/**
	 * Link to local copy of central.
	 */
	private Central central;

	/**
	 * Default constructor.
	 */
	public EmbeddedConnector() {
        central = Central.getInstance();
	}

	@Override
	protected void sendData(Snapshot snapshot) {
		central.processIncomingSnapshot(snapshot);
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}
}
