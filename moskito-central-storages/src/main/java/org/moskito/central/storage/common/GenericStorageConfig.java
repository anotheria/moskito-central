package org.moskito.central.storage.common;

import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.central.storage.helpers.IncludeExcludeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bvanchuhov
 */
@ConfigureMe
public class GenericStorageConfig {

    /**
     * Entries with producer/category/subsystem definition.
     */
    @Configure
    protected StorageConfigEntry[] entries;

    /**
     * Restored config element.
     */
    protected List<StorageConfigElement> elements;

    /**
     * Global interval include.
     */
    @Configure
    protected String includeIntervals = "*";

    /**
     * Global interval exclude.
     */
    @Configure
    protected String excludeIntervals = "";


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

    public StorageConfigEntry[] getEntries() {
        return entries;
    }

    public void setEntries(StorageConfigEntry[] entries) {
        this.entries = entries;
    }

    public boolean include(IncludeExcludeFields fields){
        for (StorageConfigElement element : elements){
            if (element.include(fields)) {
                return true;
            }
        }

        return false;
    }

    @AfterConfiguration
    public void afterConfiguration(){
        IncludeExcludeList intervals = new IncludeExcludeList(includeIntervals, excludeIntervals);
        elements = prepareStorageConfigElements(intervals);
    }

    private List<StorageConfigElement> prepareStorageConfigElements(IncludeExcludeList intervals) {
        List<StorageConfigElement> newElements = new ArrayList<>();

        if (entries == null) {
            return newElements;
        }

        for (StorageConfigEntry entry : entries){
            StorageConfigElement element = new StorageConfigElement(entry, intervals);
            newElements.add(element);
        }
        return newElements;
    }
}
