package org.moskito.central.storage.elasticsearch;

import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.central.storage.fs.IncludeExcludeList;

/**
 * @author andriiskrypnyk
 */

@ConfigureMe
public class ElasticsearchStorageConfig {

    @Configure
    private String host;

    @Configure
    private String port;

    @Configure
    private String api;

    @Configure
    private String proxy;

    @Configure
    private String clusterName;

    @Configure
    private String index;

    @Configure
    private String includeProducers = "*";

    @Configure
    private String excludeProducers = "";

    @Configure
    private String includeIntervals = "*";

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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
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

    @AfterConfiguration
    public void afterConfiguration() {
        intervals = new IncludeExcludeList(includeIntervals, excludeIntervals);
        producers = new IncludeExcludeList(includeProducers, excludeProducers);
    }

    public boolean include(String producerId, String intervalName) {
        if (!intervals.include(intervalName))
            return false;
        return producers.include(producerId);
    }

    @Override
    public String toString() {
        return "ElasticsearchStorageConfig{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", api='" + api + '\'' +
                ", proxy='" + proxy + '\'' +
                ", index='" + index + '\'' +
                ", includeProducers='" + includeProducers + '\'' +
                ", excludeProducers='" + excludeProducers + '\'' +
                ", includeIntervals='" + includeIntervals + '\'' +
                ", excludeIntervals='" + excludeIntervals + '\'' +
                '}';
    }
}
