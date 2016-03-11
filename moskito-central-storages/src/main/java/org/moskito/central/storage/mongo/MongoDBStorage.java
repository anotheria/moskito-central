package org.moskito.central.storage.mongo;

import org.configureme.ConfigurationManager;
import org.moskito.central.Snapshot;
import org.moskito.central.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author andriiskrypnyk
 */
public class MongoDBStorage implements Storage {

    private static Logger log = LoggerFactory.getLogger(MongoDBStorage.class);

    private MongoDBStorageConfig config;

    @Override
    public void configure(String configurationName) {

        config = new MongoDBStorageConfig();

        if (configurationName == null)
            return;
        try {
            ConfigurationManager.INSTANCE.configureAs(config, configurationName);
            MongoClientHolder.INSTANCE.configure(config);
        } catch (IllegalArgumentException e) {
            log.warn("Couldn't configure MongoDBStorage with " + configurationName + " , working with default values");
        }

    }

    @Override
    public void processSnapshot(Snapshot target) {
        String producerId = target.getMetaData().getProducerId();
        String interval = target.getMetaData().getIntervalName();

        if (!config.include(producerId, interval)) {
            return;
        }

        if (config.getHost() == null || config.getPort() == null) {
            log.warn("No hostname, can not save snapshot");
            return;
        }
        /*
        * store snapshots in different collections(ProducerId = collection name)
        * */
        if (config.getDistributeProducers().equals("true")) {
            config.setCollectionName(target.getMetaData().getProducerId());
        }

        MongoClientHolder.INSTANCE.storeSnapshot(target);
    }

}
