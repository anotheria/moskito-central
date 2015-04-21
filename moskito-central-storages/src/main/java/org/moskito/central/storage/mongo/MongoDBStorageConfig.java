package org.moskito.central.storage.mongo;

import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.moskito.central.storage.fs.IncludeExcludeList;

/**
 * @author andriiskrypnyk
 */
@ConfigureMe
public class MongoDBStorageConfig {

    @Configure
    private String host;

    @Configure
    private String port;

    @Configure
    private String dbName;

    @Configure
    private String login;

    @Configure
    private String password;
    /*
    * if distributeProducers set to false, will be used for collection name
    * */
    @Configure
    private String collectionName;
    /*
    * set to "true" if it needed to store all producers in different collections
    * */
    @Configure
    private String distributeProducers;

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

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getDistributeProducers() {
        return distributeProducers;
    }

    public void setDistributeProducers(String distributeProducers) {
        this.distributeProducers = distributeProducers;
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
        return "MongoDBStorageConfig{" +
                "host='" + host + '\'' +
                ", dbName='" + dbName + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", collectionName='" + collectionName + '\'' +
                ", includeProducers='" + includeProducers + '\'' +
                ", excludeProducers='" + excludeProducers + '\'' +
                ", includeIntervals='" + includeIntervals + '\'' +
                ", excludeIntervals='" + excludeIntervals + '\'' +
                ", intervals=" + intervals +
                ", producers=" + producers +
                '}';
    }
}
