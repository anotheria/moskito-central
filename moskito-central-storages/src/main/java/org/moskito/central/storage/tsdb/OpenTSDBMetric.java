package org.moskito.central.storage.tsdb;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * OpenTSDBMetric class.
 * 
 * @author esmakula
 * @since 10.10.13
 */
public class OpenTSDBMetric implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 683373966837933228L;
    /**
	 * Metric name.
	 */
	private String metric;

	/**
	 * Timestamp.
	 */
	private Long timestamp;

	/**
	 * Value.
	 */
	private Number value;

	/**
	 * Tags.
	 */
	private Map<String, String> tags = new HashMap<String, String>();

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }
}
