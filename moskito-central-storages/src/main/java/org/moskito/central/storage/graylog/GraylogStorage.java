package org.moskito.central.storage.graylog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.configureme.ConfigurationManager;
import org.moskito.central.Snapshot;
import org.moskito.central.storage.Storage;
import org.moskito.central.storage.common.IncludeExcludeFields;
import org.moskito.central.storage.helpers.SnapshotWithStatsNumbers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;

/**
 * Created by Roman Stetsiuk on 2/1/16.
 */
public class GraylogStorage implements Storage {

    private static Logger log = LoggerFactory.getLogger(GraylogStorage.class);

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Graylog storage config.
     */
    private GraylogStorageConfig config;

    /**
     * Http client
     */
    private CloseableHttpClient httpClient;

    @Override
    public void configure(String configurationName) {
        config = new GraylogStorageConfig();

        if (configurationName == null)
            return;
        try {
            ConfigurationManager.INSTANCE.configureAs(config, configurationName);
        } catch (IllegalArgumentException e) {
            log.error("Couldn't configure GraylogStorage with " + configurationName, e);
        }
    }

    private void createHttpClient() {
        if (httpClient == null) {
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            // Increase max total connection to 200
            cm.setMaxTotal(200);
            // Increase default max connection per route to 20
            cm.setDefaultMaxPerRoute(20);
            // Increase max connections for localhost:80 to 50
            HttpHost graylogHost = new HttpHost(config.getHost(), config.getPort());
            cm.setMaxPerRoute(new HttpRoute(graylogHost), 50);

            httpClient = HttpClients.custom()
                    .setConnectionManager(cm)
                    .build();
        }
    }

    @Override
    public void processSnapshot(Snapshot target) {
        String producerId = target.getMetaData().getProducerId();
        String category = target.getMetaData().getCategory();
        String subsystem = target.getMetaData().getSubsystem();
        String interval = target.getMetaData().getIntervalName();

        IncludeExcludeFields fields = new IncludeExcludeFields.Builder()
                .setProducer(producerId)
                .setCategory(category)
                .setSubsystem(subsystem)
                .setInterval(interval)
                .build();

        log.info(config.toString());

        if (config.include(fields)) {
            createHttpClient();
            HttpPost request = new HttpPost(config.getHost() + ':' + config.getPort() + '/' + config.getPath());
            try {
                String message = gson.toJson(new SnapshotWithStatsNumbers(target));

                SnapshotGelf snapshotGelf = new SnapshotGelfBuilder()
                        .host(InetAddress.getLocalHost().getHostName())
                        .shortMessage("moskito logs")
                        .message(message)
                        .build();

                HttpEntity params = new StringEntity(gson.toJson(snapshotGelf));
                request.addHeader("content-type", "application/x-www-form-urlencoded");
                request.setEntity(params);

                httpClient.execute(request);

            } catch (UnsupportedEncodingException e) {
                log.error("GraylogPublisher.sendMessage(): couldn't create StringEntity");
            } catch (IOException e) {
                log.error("GraylogPublisher.sendMessage(): couldn't execute query");
            } finally {
                request.releaseConnection();
                try {
                    httpClient.close();
                    httpClient = null;
                } catch (IOException e) {
                    log.error("couldn't close connecion to graylog server", e);
                }
            }
        }
    }

}
