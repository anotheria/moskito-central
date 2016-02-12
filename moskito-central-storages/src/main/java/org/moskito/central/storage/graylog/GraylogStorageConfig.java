package org.moskito.central.storage.graylog;

import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.central.storage.common.GenericStorageConfig;

import java.util.Arrays;

/**
 * Created by Roman Stetsiuk on 2/1/16.
 */
@ConfigureMe
public class GraylogStorageConfig extends GenericStorageConfig {

    public static final String HTTP_PREFIX = "http://";
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
        if (!host.contains(HTTP_PREFIX)) {
            host += HTTP_PREFIX;
        }
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @AfterConfiguration
    public void afterConfiguration(){
        super.afterConfiguration();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GraylogStorageConfig{");
        sb.append("host='").append(host).append('\'');
        sb.append(", port=").append(port);
        sb.append(", path='").append(path).append('\'');
        sb.append(", entries=").append(Arrays.toString(entries));
        sb.append(", elements=").append(elements);
        sb.append(", includeIntervals='").append(includeIntervals).append('\'');
        sb.append(", excludeIntervals='").append(excludeIntervals).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
