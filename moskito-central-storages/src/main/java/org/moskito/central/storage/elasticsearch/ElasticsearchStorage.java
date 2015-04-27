package org.moskito.central.storage.elasticsearch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.configureme.ConfigurationManager;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.moskito.central.Snapshot;
import org.moskito.central.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * @author andriiskrypnyk
 */
public class ElasticsearchStorage implements Storage {

    private static Logger log = LoggerFactory.getLogger(ElasticsearchStorage.class);

    private ElasticsearchStorageConfig config;

    private Client transportClient;

    private CloseableHttpClient httpClient;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();


    @Override
    public void configure(String configurationName) {


        config = new ElasticsearchStorageConfig();

        if (configurationName == null)
            return;
        try {
            ConfigurationManager.INSTANCE.configureAs(config, configurationName);
        } catch (IllegalArgumentException e) {
            log.warn("Couldn't configure ElasticsearchStorage with " + configurationName + " , working with default values");
        }

        if (config.getProxy().equals("true") && config.getApi().equals("http")) {
            config.setPort("");
        } else if (!config.getApi().equals("java")) {
            config.setHost(config.getHost() + ":");
        }

        if (config.getApi().equals("java")) {
            transportClient = getTransportClient();
        } else if (config.getApi().equals("http")) {
            httpClient = getHttpClient();
            httpPrepareIndex();
        } else {
            log.error("API isn't defined, couldn't configure ElasticsearchStorage");
        }

    }


    @Override
    public void processSnapshot(Snapshot target) {

        String producerId = target.getMetaData().getProducerId();
        String interval = target.getMetaData().getIntervalName();

        if (!config.include(producerId, interval)) {
            return;
        }

        if (transportClient != null) {
            transportProcessSnapshot(target);
        } else if (httpClient != null) {
            httpProcessSnapshot(target);
        } else {
            log.error("API isn't defined, couldn't process Snapshot");
        }

    }

    private void transportProcessSnapshot(Snapshot target) {
        IndexResponse response = transportClient.prepareIndex(config.getIndex(), target.getMetaData().getProducerId().replaceAll(" ", ""))
                .setSource(gson.toJson(target))
                .execute()
                .actionGet();

        log.debug("Stored:" + response.getId());
    }

    private void httpProcessSnapshot(Snapshot target) {
        httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(config.getHost() + config.getPort() + "/" + config.getIndex() + "/" + target.getMetaData().getProducerId().replaceAll(" ", ""));
        try {
            StringEntity entity = new StringEntity(gson.toJson(target));
            entity.setContentType("application/json");
            post.setEntity(entity);
            httpClient.execute(post);
            httpClient.close();
        } catch (Exception e) {
            log.warn("Couldn't process Snapshot: ", e);
        }


    }

    private Client getTransportClient() {

        if (transportClient == null) {
            Settings settings = ImmutableSettings.settingsBuilder()
                    .put("cluster.name", config.getClusterName())
                    .build();

            transportClient = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(config.getHost(), Integer.parseInt(config.getPort())));
        }
        return transportClient;

    }

    private CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = HttpClients.createDefault();
        }
        return httpClient;
    }

    private void httpPrepareIndex() {
        HttpPut put = new HttpPut(config.getHost() + config.getPort() + "/" + config.getIndex());
        try {
            httpClient.execute(put);
            httpClient.close();
        } catch (IOException e) {
            log.warn("Can't prepare index", e);
        }
    }
}
