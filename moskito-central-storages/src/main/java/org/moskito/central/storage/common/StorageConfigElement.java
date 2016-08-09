package org.moskito.central.storage.common;

import org.moskito.central.storage.helpers.IncludeExcludeList;
import org.moskito.central.storage.helpers.IncludeExcludeWildcardList;

/**
 * @author bvanchuhov
 */
public class StorageConfigElement {

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

    public StorageConfigElement(StorageConfigEntry entry, IncludeExcludeList outerIntervals) {
        categories = new IncludeExcludeWildcardList(entry.getIncludedCategories(), entry.getExcludedCategories());
        producers = new IncludeExcludeWildcardList(entry.getIncludedProducers(), entry.getExcludedProducers());
        subsystems = new IncludeExcludeWildcardList(entry.getIncludedSubsystems(), entry.getExcludedSubsystems());

        if ((entry.getIncludedIntervals() == null || entry.getIncludedIntervals().isEmpty()) &&
                (entry.getExcludedIntervals() == null || entry.getExcludedIntervals().isEmpty())) {
            intervals = outerIntervals;
        } else {
            intervals = new IncludeExcludeList(entry.getIncludedIntervals(), entry.getExcludedIntervals());
        }
    }

    public boolean include(IncludeExcludeFields fields) {
        return producers.include(fields.getProducer()) &&
                categories.include(fields.getCategory()) &&
                intervals.include(fields.getInterval()) &&
                subsystems.include(fields.getSubsystem());
    }

    @Override
    public String toString() {
        return "StorageConfigElement{" +
                "intervals=" + intervals +
                ", producers=" + producers +
                ", categories=" + categories +
                ", subsystems=" + subsystems +
                '}';
    }
}
