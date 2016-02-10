package org.moskito.central.storage.graylog;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

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
    private String path;

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

    @Override
    public String toString() {
        return "GraylogStorageConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", path='" + path + '\'' +
                '}';
    }
}
