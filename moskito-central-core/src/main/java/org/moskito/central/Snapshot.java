package org.moskito.central;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Represents a single snapshot.
 *
 * @author lrosenberg
 * @since 15.03.13 23:15
 */
@XmlRootElement(name = "snsh")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ HashMap.class })
public class Snapshot implements Serializable {

	/**
	 * Serialversion uid.
	 */
	private static final long serialVersionUID = -1452811059878773956L;

	/**
	 * The metadata. The metadata contains data about the snapshot like
	 * producerId or timestamp.
	 */
	@XmlElement(name = "snshmd")
	private SnapshotMetaData metaData;

	/**
	 * Stat values.
	 */
	@XmlJavaTypeAdapter(StatsMapAdapter.class)
	private Map<String, Map<String, String>> stats = new HashMap<>();

	/**
	 * Default constructor.
	 */
	public Snapshot() {

	}

	public SnapshotMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(SnapshotMetaData metaData) {
		this.metaData = metaData;
	}

	/**
	 * Adds snapshot data to the stats map.
	 *
	 * @param name
	 * @param values
	 */
	public void addSnapshotData(String name, Map<String, String> values) {
		stats.put(name, values);
	}

	@Override
	public String toString() {
		return "Snapshot [metaData=" + metaData + ", stats=" + stats + "]";
	}

	/**
	 * Gets statistics by specified {@link String} key.
	 *
	 * @param stat
	 * @return {@link Map}
	 */
	public Map<String, String> getStatistics(String stat) {
		return stats.get(stat);
	}

	public Set<Entry<String, Map<String, String>>> getEntrySet() {
		return stats.entrySet();
	}

	public Set<String> getKeySet() {
		return stats.keySet();
	}

	/**
	 * Gets all statistics.
	 *
	 * @return {@link Map}
	 */
	public Map<String, Map<String, String>> getStats() {
		if (stats == null) {
			stats = new HashMap<>();
		}
		return stats;
	}

	public void setStats(Map<String, Map<String, String>> stats) {
		this.stats = stats;
	}

	/**
	 * XmlAdapter for String, HashMap&lt;String, String&gt;.
	 *
	 * @author dagafonov
	 *
	 */
	public static class StatsMapAdapter extends MapAdapter<String, Map<String, String>> {

	}

}
