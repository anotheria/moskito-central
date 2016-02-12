package org.moskito.central.storage.common;

import org.moskito.central.SnapshotMetaData;

/**
 * Fields for configuring exclude/include
 *
 * @author Roman Stetsiuk
 * @author bvanchuhov
 */
public final class IncludeExcludeFields {

    private final String producer;
    private final String category;
    private final String subsystem;
    private final String interval;

    public IncludeExcludeFields(SnapshotMetaData metaData) {
        this.producer = metaData.getProducerId();
        this.category = metaData.getCategory();
        this.subsystem = metaData.getSubsystem();
        this.interval = metaData.getIntervalName();
    }

    private IncludeExcludeFields(Builder builder) {
        this.producer = builder.producer;
        this.category = builder.category;
        this.subsystem = builder.subsystem;
        this.interval = builder.interval;
    }

    public String getProducer() {
        return producer;
    }

    public String getCategory() {
        return category;
    }

    public String getSubsystem() {
        return subsystem;
    }

    public String getInterval() {
        return interval;
    }

    public static class Builder {

        private String producer;
        private String category;
        private String subsystem;
        private String interval;

        public Builder setProducer(String producer) {
            this.producer = producer;
            return this;
        }

        public Builder setCategory(String category) {
            this.category = category;
            return this;
        }

        public Builder setSubsystem(String subsystem) {
            this.subsystem = subsystem;
            return this;
        }

        public Builder setInterval(String interval) {
            this.interval = interval;
            return this;
        }

        public IncludeExcludeFields build() {
            return new IncludeExcludeFields(this);
        }
    }
}
