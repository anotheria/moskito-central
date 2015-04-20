package org.moskito.central.storage.mongo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.configureme.ConfigurationManager;
import org.moskito.central.Snapshot;
import org.moskito.central.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author andriiskrypnyk
 */
public class MongoDBStorage implements Storage {

    private static Logger log = LoggerFactory.getLogger(MongoDBStorage.class);

    private MongoDBStorageConfig config;

    private Gson gson = new GsonBuilder().create();


    @Override
    public void configure(String configurationName) {

        config = new MongoDBStorageConfig();

        if (configurationName == null)
            return;
        try {
            ConfigurationManager.INSTANCE.configureAs(config, configurationName);
        } catch (IllegalArgumentException e) {
            log.warn("Couldn't configure MongoDBStorage with " + configurationName + " , working with default values");
        }

    }

    @Override
    public void processSnapshot(Snapshot target) {
        String producerId = target.getMetaData().getProducerId();
        String interval = target.getMetaData().getIntervalName();
        String collectionName = config.getCollectionName();

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
            collectionName = target.getMetaData().getProducerId();
        }

        MongoCredential credential = MongoCredential.createCredential(config.getLogin(), config.getDbName(), config.getPassword().toCharArray());
        MongoClient mongoClient = null;

        try {
            mongoClient = new MongoClient(new ServerAddress(config.getHost(), Integer.parseInt(config.getPort())), Arrays.asList(credential));
            MongoDatabase db = mongoClient.getDatabase(config.getDbName());
            MongoCollection collection = db.getCollection(collectionName);
            String json = gson.toJson(target);
            collection.insertOne(Document.parse(json));
        } finally {
            mongoClient.close();
        }

    }

}
