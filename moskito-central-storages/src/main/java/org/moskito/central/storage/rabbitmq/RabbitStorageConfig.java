package org.moskito.central.storage.rabbitmq;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.central.storage.fs.IncludeExcludeList;

/**
 * Created by Roman Stetsiuk on 2/1/16.
 */
@ConfigureMe
public class RabbitStorageConfig {
    /**
     * RabbitMQ host
     */
    @Configure
    private String host;

    /**
     * RabbitMQ port
     */
    @Configure
    private int port;
    /**
     * RabbitMQ queye name
     */
    @Configure
    private String queueName;

    /**
     * RabbitMQ user
     */
    @Configure
    private String user;

    /**
     * RabbitMQ user password
     */
    @Configure
    private String password;

    /**
     * includeProducers.
     */
    @Configure
    private String includeProducers = "*";
    /**
     * excludeProducers.
     */
    @Configure
    private String excludeProducers = "";
    /**
     * includeIntervals.
     */
    @Configure
    private String includeIntervals = "*";
    /**
     * excludeIntervals.
     */
    @Configure
    private String excludeIntervals = "";

    private IncludeExcludeList intervals;

    private IncludeExcludeList producers;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIncludeProducers() {
        return includeProducers;
    }

    public void setIncludeProducers(String includeProducers) {
        this.includeProducers = includeProducers;
    }

    public String getExcludeProducers() {
        return excludeProducers;
    }

    public void setExcludeProducers(String excludeProducers) {
        this.excludeProducers = excludeProducers;
    }

    public String getIncludeIntervals() {
        return includeIntervals;
    }

    public void setIncludeIntervals(String includeIntervals) {
        this.includeIntervals = includeIntervals;
    }

    public String getExcludeIntervals() {
        return excludeIntervals;
    }

    public void setExcludeIntervals(String excludeIntervals) {
        this.excludeIntervals = excludeIntervals;
    }

    public IncludeExcludeList getIntervals() {
        return intervals;
    }

    public void setIntervals(IncludeExcludeList intervals) {
        this.intervals = intervals;
    }

    public IncludeExcludeList getProducers() {
        return producers;
    }

    public void setProducers(IncludeExcludeList producers) {
        this.producers = producers;
    }

    @Override
    public String toString() {
        return "RabbitStorageConfig{" +
                "host='" + host + '\'' +
                ", queueName='" + queueName + '\'' +
                ", port='" + port + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", includeProducers='" + includeProducers + '\'' +
                ", excludeProducers='" + excludeProducers + '\'' +
                ", includeIntervals='" + includeIntervals + '\'' +
                ", excludeIntervals='" + excludeIntervals + '\'' +
                ", intervals=" + intervals +
                ", producers=" + producers +
                '}';
    }
}
