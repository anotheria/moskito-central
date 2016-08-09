package org.moskito.central.storage.fs;

import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.central.storage.helpers.IncludeExcludeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Configuration file for CSVFile Storage.
 *
 * @author lrosenberg
 * @since 25.03.13 10:00
 */
@ConfigureMe
public class CSVFileStorageConfig {
	
	/**
	 * Entries with producer/stat/interval definition.
	 */
	@Configure 
	private CSVFileStorageConfigEntry[] entries;

	/**
	 * File pattern.
	 */
	@Configure
	private String pattern;

	/**
	 * Restored config element.
	 */
	private List<CSVFileStorageConfigElement> elements;

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


	public CSVFileStorageConfigEntry[] getEntries() {
		return entries;
	}

	public void setEntries(CSVFileStorageConfigEntry[] entries) {
		this.entries = entries;
	}

	@Override public String toString(){
        return "Pattern: "+ pattern +", Entries: "+ Arrays.toString(entries)+
				", InclIntervals: "+includeIntervals+
				", ExclIntervals: "+excludeIntervals;

	}

	public boolean include(String producer, String stat, String interval){
		List<CSVFileStorageConfigElement> listCopy = elements;
		if (listCopy==null)
			return false;
		for (CSVFileStorageConfigElement e : listCopy){
			if (e.include(producer, stat, interval))
				return true;

		}
		return false;
	}

	@AfterConfiguration
	public void afterConfig(){
		IncludeExcludeList intervals = new IncludeExcludeList(includeIntervals, excludeIntervals);
		List<CSVFileStorageConfigElement> newElements = new ArrayList<>();
		for (CSVFileStorageConfigEntry entry : entries){
			CSVFileStorageConfigElement element = new CSVFileStorageConfigElement(entry, intervals);
			newElements.add(element);
		}
		elements = newElements;

	}

	/**
	 * Runtime used element.
	 */
	private static class CSVFileStorageConfigElement{
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

		public CSVFileStorageConfigElement(CSVFileStorageConfigEntry entry, IncludeExcludeList outerIntervals){
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
			return "CSVFileStorageConfigElement{" +
					"intervals=" + intervals +
					", producers=" + producers +
					", stats=" + stats +
					'}';
		}
	}
}
