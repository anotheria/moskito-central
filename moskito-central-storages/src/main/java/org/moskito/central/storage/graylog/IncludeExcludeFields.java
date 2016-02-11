package org.moskito.central.storage.graylog;

/**
 * Fields for configuring exclude/include
 *
 * Created by Roman Stetsiuk on 2/11/16.
 */
public class IncludeExcludeFields {
    private String producer;
    private String category;
    private String subsystem;
    private String interval;

    public IncludeExcludeFields() {
    }

    public IncludeExcludeFields(String producer, String category, String subsystem, String interval) {
        this.producer = producer;
        this.category = category;
        this.subsystem = subsystem;
        this.interval = interval;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    @Override
    public String toString() {
        return "IncludeExcludeFields{" +
                "producer='" + producer + '\'' +
                ", category='" + category + '\'' +
                ", subsystem='" + subsystem + '\'' +
                ", interval='" + interval + '\'' +
                '}';
    }
}
