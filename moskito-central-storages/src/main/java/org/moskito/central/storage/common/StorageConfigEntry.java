package org.moskito.central.storage.common;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * Created by Roman Stetsiuk on 2/11/16.
 */
@ConfigureMe
public class StorageConfigEntry {
    /**
     * Included interval names, comma separated or '*'.
     */
    @Configure
    private String includedIntervals = "*";

    /**
     * Excluded interval names, comma separated.
     */
    @Configure
    private String excludedIntervals = "";

    /**
     * Included producer names, comma separated or '*'.
     */
    @Configure
    private String includedProducers = "*";

    /**
     * Excluded producer names, comma separated.
     */
    @Configure
    private String excludedProducers = "";

    /**
     * Included category names, comma separated or '*'.
     */
    @Configure
    private String includedCategories = "*";

    /**
     * Excluded category names, comma separated.
     */
    @Configure
    private String excludedCategories = "";

     /**
     * Included subsystem names, comma separated or '*'.
     */
    @Configure
    private String includedSubsystems = "*";

    /**
     * Excluded subsystems names, comma separated.
     */
    @Configure
    private String excludedSubsystems = "";

    public String getIncludedIntervals() {
        return includedIntervals;
    }

    public void setIncludedIntervals(String includedIntervals) {
        this.includedIntervals = includedIntervals;
    }

    public String getExcludedIntervals() {
        return excludedIntervals;
    }

    public void setExcludedIntervals(String excludedIntervals) {
        this.excludedIntervals = excludedIntervals;
    }

    public String getIncludedProducers() {
        return includedProducers;
    }

    public void setIncludedProducers(String includedProducers) {
        this.includedProducers = includedProducers;
    }

    public String getExcludedProducers() {
        return excludedProducers;
    }

    public void setExcludedProducers(String excludedProducers) {
        this.excludedProducers = excludedProducers;
    }

    public String getIncludedCategories() {
        return includedCategories;
    }

    public void setIncludedCategories(String includedCategories) {
        this.includedCategories = includedCategories;
    }

    public String getExcludedCategories() {
        return excludedCategories;
    }

    public void setExcludedCategories(String excludedCategories) {
        this.excludedCategories = excludedCategories;
    }

    public String getIncludedSubsystems() {
        return includedSubsystems;
    }

    public void setIncludedSubsystems(String includedSubsystems) {
        this.includedSubsystems = includedSubsystems;
    }

    public String getExcludedSubsystems() {
        return excludedSubsystems;
    }

    public void setExcludedSubsystems(String excludedSubsystems) {
        this.excludedSubsystems = excludedSubsystems;
    }

    @Override
    public String toString() {
        return "StorageConfigEntry{" +
                "includedIntervals='" + includedIntervals + '\'' +
                ", excludedIntervals='" + excludedIntervals + '\'' +
                ", includedProducers='" + includedProducers + '\'' +
                ", excludedProducers='" + excludedProducers + '\'' +
                ", includedCategories='" + includedCategories + '\'' +
                ", excludedCategories='" + excludedCategories + '\'' +
                ", includedSubsystems='" + includedSubsystems + '\'' +
                ", excludedSubsystems='" + excludedSubsystems + '\'' +
                '}';
    }


}

