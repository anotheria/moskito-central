package org.moskito.central.storage.graylog;

import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.central.storage.helpers.IncludeExcludeList;
import org.moskito.central.storage.helpers.IncludeExcludeWildcardList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Roman Stetsiuk on 2/1/16.
 */
@ConfigureMe
public class GraylogStorageConfig {
    /**
     * Graylog host
     */
    @Configure
    private String host;

    /**
     * Graylog port
     */
    @Configure
    private int port;

    /**
     * Gelf path
     */
    @Configure
    private String path = "gelf";

    /**
     * Entries with producer/category/subsystem definition.
     */
    @Configure
    private GraylogStorageConfigEntry[] entries;

    /**
     * Restored config element.
     */
    private List<GraylogStorageConfigElement> elements;

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



    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {

        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExcludeIntervals() {
        return excludeIntervals;
    }

    public void setExcludeIntervals(String excludeIntervals) {
        this.excludeIntervals = excludeIntervals;
    }

    public String getIncludeIntervals() {
        return includeIntervals;
    }

    public void setIncludeIntervals(String includeIntervals) {
        this.includeIntervals = includeIntervals;
    }

    public GraylogStorageConfigEntry[] getEntries() {
        return entries;
    }

    public void setEntries(GraylogStorageConfigEntry[] entries) {
        this.entries = entries;
    }

    public boolean include(IncludeExcludeFields fields){
        List<GraylogStorageConfigElement> listCopy = elements;
        if (listCopy==null)
            return false;
        for (GraylogStorageConfigElement e : listCopy){
            if (e.include(fields))
                return true;

        }
        return false;
    }

    @AfterConfiguration
    public void afterConfig(){
        IncludeExcludeList intervals = new IncludeExcludeList(includeIntervals, excludeIntervals);
        List<GraylogStorageConfigElement> newElements = new ArrayList<>();
        for (GraylogStorageConfigEntry entry : entries){
            GraylogStorageConfigElement element = new GraylogStorageConfigElement(entry, intervals);
            newElements.add(element);
        }
        elements = newElements;

    }

    /**
     * Runtime used element.
     */
    private static class GraylogStorageConfigElement{
        /**
         * Include/Exclude list with intervals.
         */
        private IncludeExcludeList intervals;
        /**
         * Include/Exclude list with producers.
         */
        private IncludeExcludeWildcardList producers;
        /**
         * Include/Exclude list with categories.
         */
        private IncludeExcludeWildcardList categories;
        /**
         * Include/Exclude list with subsystems.
         */
        private IncludeExcludeWildcardList subsystems;

        public GraylogStorageConfigElement(GraylogStorageConfigEntry entry, IncludeExcludeList outerIntervals){
            categories = new IncludeExcludeWildcardList(entry.getIncludedCategories(), entry.getExcludedCategories());
            producers = new IncludeExcludeWildcardList(entry.getIncludedProducers(), entry.getExcludedProducers());
            subsystems = new IncludeExcludeWildcardList(entry.getIncludedSubsystems(), entry.getExcludedSubsystems());

            if ((entry.getIncludedIntervals()==null || entry.getIncludedIntervals().length()==0) &&
                    (entry.getExcludedIntervals()==null || entry.getExcludedIntervals().length()==0)){
                intervals = outerIntervals;
            }else{
                intervals = new IncludeExcludeList(entry.getIncludedIntervals(), entry.getExcludedIntervals());
            }
        }

        public boolean include(IncludeExcludeFields fields){
            return producers.include(fields.getProducer()) &&
                    categories.include(fields.getCategory()) &&
                    intervals.include(fields.getInterval()) &&
                    subsystems.include(fields.getSubsystem());
        }

        @Override
        public String toString() {
            return "GraylogStorageConfigElement{" +
                    "intervals=" + intervals +
                    ", producers=" + producers +
                    ", categories=" + categories +
                    ", subsystems=" + subsystems +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "GraylogStorageConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", path='" + path + '\'' +
                ", entries=" + Arrays.toString(entries) +
                ", elements=" + elements +
                ", includeIntervals='" + includeIntervals + '\'' +
                ", excludeIntervals='" + excludeIntervals + '\'' +
                '}';
    }
}
