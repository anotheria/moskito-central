package org.moskito.central.connectors;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.moskito.central.CentralConstants;
import org.moskito.central.Snapshot;
import org.moskito.central.SnapshotMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.anotheria.moskito.core.plugins.AbstractMoskitoPlugin;
import net.anotheria.moskito.core.snapshot.ProducerSnapshot;
import net.anotheria.moskito.core.snapshot.SnapshotConsumer;
import net.anotheria.moskito.core.snapshot.SnapshotRepository;
import net.anotheria.moskito.core.snapshot.StatSnapshot;
import net.anotheria.net.util.NetUtils;

/**
 * Parent class for all central connectors. Describes full logic.
 * 
 * @author dagafonov
 * 
 */
public abstract class AbstractCentralConnector extends AbstractMoskitoPlugin implements SnapshotConsumer {

	/**
	 * Name of the component. Default is app.
	 */
	private String componentName = "app";

	/**
	 * Hostname.
	 */
	private String host;

	/**
	 * Default intervals.
	 */
	private Set<String> defaultIntervals = Collections.emptySet();

	/**
	 * Logger instance.
	 */
	private final static Logger log = LoggerFactory.getLogger(AbstractCentralConnector.class);

	/**
	 * Default constructor.
	 */
	public AbstractCentralConnector() {
		componentName = System.getProperty(CentralConstants.PROP_COMPONENT, componentName);
		try {
			host = NetUtils.getShortComputerName();
		} catch (Exception ignored) {
		}
		if (host == null)
			host = "unknown";
		host = System.getProperty(CentralConstants.PROP_HOSTNAME, host);
	}

	public void configure(AbstractCentralConnectorConfig config) {
		if (config.getSupportedIntervals()==null || config.getSupportedIntervals().length==0){
			return;
		}

		defaultIntervals = new HashSet<>(Arrays.asList(config.getSupportedIntervals()));
	}

	@Override
	public void initialize() {
		super.initialize();
		SnapshotRepository.getInstance().addConsumer(this);
	}

	@Override
	public void deInitialize() {
		SnapshotRepository.getInstance().removeConsumer(this);
		super.deInitialize();
	}

	@Override
	public void consumeSnapshot(ProducerSnapshot coreSnapshot) {
		if (!defaultIntervals.isEmpty() && !defaultIntervals.contains(coreSnapshot.getIntervalName())) {
			return;
		}

		Snapshot centralSnapshot = makeSnapshot(coreSnapshot);
		log.debug(this.getClass().getName() + ": \r\n" + centralSnapshot);
		try {
			sendData(centralSnapshot);
		} catch (Exception e) {
			if(log.isDebugEnabled()) {
				log.error(this.getClass().getSimpleName() + ".sendData() failed", e);
			} else {
				log.error(this.getClass().getSimpleName() + ".sendData() failed: " + e.getMessage());
			}
		}
	}

	private Snapshot makeSnapshot(ProducerSnapshot coreSnapshot) {
		Snapshot centralSnapshot = new Snapshot();

		SnapshotMetaData metaData = new SnapshotMetaData();
		metaData.setProducerId(coreSnapshot.getProducerId());
		metaData.setCategory(coreSnapshot.getCategory());
		metaData.setSubsystem(coreSnapshot.getSubsystem());

		metaData.setComponentName(componentName);
		metaData.setHostName(host);
		metaData.setIntervalName(coreSnapshot.getIntervalName());

		metaData.setCreationTimestamp(coreSnapshot.getTimestamp());
		metaData.setStatClassName(coreSnapshot.getStatClassName());
		centralSnapshot.setMetaData(metaData);

		Map<String, StatSnapshot> coreStatSnapshots = coreSnapshot.getStatSnapshots();
		for (Map.Entry<String, StatSnapshot> coreStatSnapshot : coreStatSnapshots.entrySet()) {
			centralSnapshot.addSnapshotData(coreStatSnapshot.getKey(), new HashMap<String, String>(coreStatSnapshot.getValue().getValues()));
		}
		return centralSnapshot;
	}

	/**
	 * Connector specific implementation.
	 * 
	 * @param snapshot
	 */
	protected abstract void sendData(Snapshot snapshot);

	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + componentName + "@" + host;
	}

}
