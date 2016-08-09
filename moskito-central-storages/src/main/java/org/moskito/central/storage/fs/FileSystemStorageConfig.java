package org.moskito.central.storage.fs;

import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.central.storage.common.GenericStorageConfig;
import org.moskito.central.storage.common.IncludeExcludeFields;
import org.moskito.central.storage.helpers.IncludeExcludeList;

/**
 * FileSystemStorageConfig class.
 *
 * @author lrosenberg
 * @since 22.03.13 14:15
 */
@ConfigureMe
public class FileSystemStorageConfig extends GenericStorageConfig {

	/**
	 * Serializer name.
	 */
	@Configure
	private String serializer;

	/**
	 * excludeProducers.
	 */
	@Configure
	private String includeProducers = "*";

	/**
	 * excludeProducers.
	 */
	@Configure
	private String excludeProducers = "";

	/**
	 * Pattern.
	 */
	@Configure
	private String pattern = "/tmp/central/{host}/{component}/{producer}/{date}/{date}_{time}_{producer}.json";

	/**
	 * includeIntervals.
	 */
	@Configure
	private String includeIntervals = "*";

	/**
	 * excludeIntervals.
	 */
	@Configure
	private String excludeIntervals = "";

	/**
	 * intervals.
	 */
	private IncludeExcludeList intervals;

	/**
	 * producers.
	 */
	private IncludeExcludeList producers;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getIncludeIntervals() {
		return includeIntervals;
	}

	public void setIncludeIntervals(String includeIntervals) {
		this.includeIntervals = includeIntervals;
	}

	public String getExcludeIntervals() {
		return excludeIntervals;
	}

	public void setExcludeIntervals(String excludeIntervals) {
		this.excludeIntervals = excludeIntervals;
	}

	public String getSerializer() {
		return serializer;
	}

	public void setSerializer(String serializer) {
		this.serializer = serializer;
	}

	public String getIncludeProducers() {
		return includeProducers;
	}

	public void setIncludeProducers(String includeProducers) {
		this.includeProducers = includeProducers;
	}

	public String getExcludeProducers() {
		return excludeProducers;
	}

	public void setExcludeProducers(String excludeProducers) {
		this.excludeProducers = excludeProducers;
	}

	/**
	 * afterConfiguration.
	 */
	@AfterConfiguration
    @Override
	public void afterConfiguration() {
        super.afterConfiguration();
		intervals = new IncludeExcludeList(includeIntervals, excludeIntervals);
		producers = new IncludeExcludeList(includeProducers, excludeProducers);
	}

	@Override
	public String toString() {
        return "Pat: " + pattern + ", InclIntervals: " + includeIntervals + ", ExclIntervals: " + excludeIntervals + ", Ser: "
				+ serializer + ", InclProducers: " + includeProducers + ", ExclProducers: " + excludeProducers;
	}

	/**
	 * Checks on availability producerId and interval.
	 *
	 * @param producerId
	 * @param intervalName
	 * @return boolean.
	 */
	public boolean include(IncludeExcludeFields fields, String producerId, String intervalName) {
		if (!super.include(fields)) return false;
        if (!intervals.include(intervalName)) return false;

		return producers.include(producerId);
	}
}
