package org.moskito.central.storage.mongo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.moskito.central.Snapshot;

import java.util.Collections;

/**
 * @author andriiskrypnyk
 */
public enum MongoClientHolder {
    INSTANCE;

    private Gson gson = new GsonBuilder().create();

    private MongoDBStorageConfig config;

    private MongoClient client;

    public void configure(MongoDBStorageConfig config) {
        this.config = config;
        MongoCredential credential = MongoCredential.createCredential(config.getLogin(), config.getDbName(), config.getPassword().toCharArray());
        client = new MongoClient(new ServerAddress(config.getHost(), Integer.parseInt(config.getPort())), Collections.singletonList(credential));
    }

    public void storeSnapshot(Snapshot target) {
        MongoDatabase db = client.getDatabase(config.getDbName());
        MongoCollection collection = db.getCollection(config.getCollectionName());
        String json = gson.toJson(target);
        collection.insertOne(Document.parse(json));
    }

}
