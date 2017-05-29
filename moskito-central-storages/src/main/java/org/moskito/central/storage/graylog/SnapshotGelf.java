package org.moskito.central.storage.graylog;

/**
 * Created by Roman Stetsiuk on 2/10/16.
 */
public class SnapshotGelf {
    private String host;
    private long timestamp;
    private String short_message;
    private String data;

    public void setHost(String host) {
        this.host = host;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setShortMessage(String short_message) {
        this.short_message = short_message;
    }


    public String getHost() {
        return host;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getShortMessage() {
        return short_message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SnapshotGelf{" +
                "host='" + host + '\'' +
                ", short_message='" + short_message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
