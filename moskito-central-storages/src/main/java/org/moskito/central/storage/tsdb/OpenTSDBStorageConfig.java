package org.moskito.central.storage.tsdb;

import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.central.storage.helpers.IncludeExcludeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Configuration file for OpenTSDB Storage.
 *
 * @author esmakula
 * @since 10.10.13
 */
@ConfigureMe
public class OpenTSDBStorageConfig {

    /**
     * File pattern.
     */
    @Configure
    private String url;
    /**
	 * Entries with producer/stat/interval definition.
	 */
	@Configure 
	private OpenTSDBStorageConfigEntry[] entries;

	/**
	 * Restored config element.
	 */
	private List<OpenTSDBStorageConfigElement> elements;

	/**
	 * Global interval include.
	 */
	@Configure
	private String includeIntervals = "*";

	/**
	 * Global interval exclude.
	 */
	@Configure
	private String excludeIntervals = "";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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


	public OpenTSDBStorageConfigEntry[] getEntries() {
		return entries;
	}

	public void setEntries(OpenTSDBStorageConfigEntry[] entries) {
		this.entries = entries;
	}

	@Override public String toString(){
        return "Url: " + url + ", Entries: " + Arrays.toString(entries) +
				", InclIntervals: " + includeIntervals +
				", ExclIntervals: " + excludeIntervals;

	}

	public boolean include(String producer, String stat, String interval){
		List<OpenTSDBStorageConfigElement> listCopy = elements;
		if (listCopy==null)
			return false;
		for (OpenTSDBStorageConfigElement e : listCopy){
			if (e.include(producer, stat, interval))
				return true;

		}
		return false;
	}

	@AfterConfiguration
	public void afterConfig(){
		IncludeExcludeList intervals = new IncludeExcludeList(includeIntervals, excludeIntervals);
		List<OpenTSDBStorageConfigElement> newElements = new ArrayList<>();
		for (OpenTSDBStorageConfigEntry entry : entries){
			OpenTSDBStorageConfigElement element = new OpenTSDBStorageConfigElement(entry, intervals);
			newElements.add(element);
		}
		elements = newElements;

	}

	/**
	 * Runtime used element.
	 */
	private static class OpenTSDBStorageConfigElement {
		/**
		 * Include/Exclude list with intervals.
		 */
		private IncludeExcludeList intervals;
		/**
		 * Include/Exclude list with producers.
		 */
		private IncludeExcludeList producers;
		/**
		 * Include/Exclude list with stats.
		 */
		private IncludeExcludeList stats;

		public OpenTSDBStorageConfigElement(OpenTSDBStorageConfigEntry entry, IncludeExcludeList outerIntervals){
			stats = new IncludeExcludeList(entry.getIncludedStats(), entry.getExcludedStats());
			producers = new IncludeExcludeList(entry.getIncludedProducers(), entry.getExcludedProducers());
			if ((entry.getIncludedIntervals()==null || entry.getIncludedIntervals().isEmpty()) &&
				(entry.getExcludedIntervals()==null || entry.getExcludedIntervals().isEmpty())){
				intervals = outerIntervals;
			}else{
				intervals = new IncludeExcludeList(entry.getIncludedIntervals(), entry.getExcludedIntervals());
			}
		}

		public boolean include(String producer, String stat, String interval){
			return producers.include(producer) &&
					stats.include(stat) && intervals.include(interval);
		}

		@Override
		public String toString() {
			return "OpenTSDBStorageConfigElement{" +
					"intervals=" + intervals +
					", producers=" + producers +
					", stats=" + stats +
					'}';
		}
	}
}
