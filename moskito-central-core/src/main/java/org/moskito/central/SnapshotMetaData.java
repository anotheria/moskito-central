package org.moskito.central;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Contains meta data about the snapshot like producerId, creation timestamp and
 * so on.
 *
 * @author lrosenberg
 * @since 20.03.13 14:07
 */
@XmlRootElement(name = "snshmd")
@XmlAccessorType(XmlAccessType.FIELD)
public class SnapshotMetaData implements Serializable {
	/**
	 * Id of the producer.
	 */
	private String producerId;

	/**
	 * Name of the component.
	 */
	private String componentName;

	/**
	 * Hostname.
	 */
	private String hostName;

	/**
	 * Intervalname.
	 */
	private String intervalName;

	/**
	 * Timestamp when the snapshot was created.
	 */
	private long creationTimestamp;

	/**
	 * Timestamp when the snapshot arrived in central.
	 */
	private long arrivalTimestamp;

	/**
	 * Category of the producer.
	 */
	private String category;
	/**
	 * Subsystem of the producer.
	 */
	private String subsystem;

	/**
	 *
	 */
	private String statClassName;

	/**
	 * Default constructor.
	 */
	public SnapshotMetaData() {
		arrivalTimestamp = System.currentTimeMillis();
	}

    public String getProducerId() {
        return producerId;
    }

    public SnapshotMetaData setProducerId(String producerId) {
        this.producerId = producerId;
        return this;
    }

    public String getComponentName() {
        return componentName;
    }

    public SnapshotMetaData setComponentName(String componentName) {
        this.componentName = componentName;
        return this;
    }

    public String getHostName() {
        return hostName;
    }

    public SnapshotMetaData setHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public String getIntervalName() {
        return intervalName;
    }

    public SnapshotMetaData setIntervalName(String intervalName) {
        this.intervalName = intervalName;
        return this;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public SnapshotMetaData setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
        return this;
    }

    public long getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public SnapshotMetaData setArrivalTimestamp(long arrivalTimestamp) {
        this.arrivalTimestamp = arrivalTimestamp;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public SnapshotMetaData setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getSubsystem() {
        return subsystem;
    }

    public SnapshotMetaData setSubsystem(String subsystem) {
        this.subsystem = subsystem;
        return this;
    }

    public String getStatClassName() {
        return statClassName;
    }

    public SnapshotMetaData setStatClassName(String statClassName) {
        this.statClassName = statClassName;
        return this;
    }

    @Override
	public String toString() {
		return "SnapshotMetaData [producerId=" + producerId + ", componentName=" + componentName + ", hostName=" + hostName + ", intervalName="
				+ intervalName + ", creationTimestamp=" + creationTimestamp + ", arrivalTimestamp=" + arrivalTimestamp + ", category=" + category
				+ ", subsystem=" + subsystem + ", statClassName=" + statClassName + "]";
	}
}
