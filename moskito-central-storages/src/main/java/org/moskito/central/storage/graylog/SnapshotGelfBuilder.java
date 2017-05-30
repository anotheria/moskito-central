package org.moskito.central.storage.graylog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roman Stetsiuk on 2/10/16.
 */
public class SnapshotGelfBuilder {
    private final String version = "1.1";
    private String host;
    private long timestamp;
    private String short_message;
    private String message;
    private Map<String, Object> fields = new HashMap<>();

    public SnapshotGelfBuilder() {
    }

    public SnapshotGelfBuilder message(final String message) {
        this.message = message;
        return this;
    }

    public SnapshotGelfBuilder host(final String host) {
        this.host = host;
        return this;
    }

    public SnapshotGelfBuilder timestamp(final long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public SnapshotGelfBuilder shortMessage(final String short_message) {
        this.short_message = short_message;
        return this;
    }

    public SnapshotGelfBuilder additionalField(final String key, final Object value) {
        fields.put(key, value);
        return this;
    }

    public SnapshotGelfBuilder additionalFields(final Map<String, Object> additionalFields) {
        fields.putAll(additionalFields);
        return this;
    }

    public String getVersion() {
        return version;
    }

    public String getHost() {
        return host;
    }

    public String getShortMessage() {
        return short_message;
    }

    public String getMessage() {
        return message;
    }

    public SnapshotGelf build() {
        if (message == null) {
            throw new IllegalArgumentException("message must not be null!");
        }

        if (host == null || host.trim().isEmpty()) {
            throw new IllegalArgumentException("host must not be null or empty!");
        }

        final SnapshotGelf snapshotGelf = new SnapshotGelf();
        snapshotGelf.setHost(host);
        snapshotGelf.setTimestamp(timestamp / 1000);
        snapshotGelf.setShortMessage(short_message);
        snapshotGelf.setData(message);

        return snapshotGelf;
    }

}
