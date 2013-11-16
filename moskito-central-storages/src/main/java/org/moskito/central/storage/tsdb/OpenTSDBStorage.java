package org.moskito.central.storage.tsdb;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.configureme.ConfigurationManager;
import org.moskito.central.Snapshot;
import org.moskito.central.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * OpenTSDB snapshot storage implementation.
 * 
 * @author esmakula
 * @since 10.10.13
 */
public class OpenTSDBStorage implements Storage {

	/**
	 * Storage config.
	 */
	private OpenTSDBStorageConfig config;

	/**
	 * Logger instance.
	 */
	private static Logger log = LoggerFactory.getLogger(OpenTSDBStorage.class);

	/**
	 * Default constructor.
	 */
	public OpenTSDBStorage() {
	}

    /**
     * Helper instance.
     */
    private OpenTSDBHelper helper = new OpenTSDBHelper();

    @Override
    public void configure(String configurationName) {
        config = new OpenTSDBStorageConfig();
        if (configurationName==null)
            return;
        try{
            ConfigurationManager.INSTANCE.configureAs(config, configurationName);
        }catch(IllegalArgumentException e){
            log.warn("Couldn't configure OpenTSDBStorage with " + configurationName + " , working with default values");
        }
        log.info("Configured OpenTSDBStorage "+config+" from configuration file "+configurationName);
    }

	@Override
    public void processSnapshot(Snapshot target) {
        String producerId = target.getMetaData().getProducerId();
        String interval = target.getMetaData().getIntervalName();
        Set<String> stats = target.getKeySet();
        if (stats==null || stats.size()==0)
            return;
        Map<String, String> tags = helper.getTags(target);
        List<OpenTSDBMetric> metrics = new ArrayList<OpenTSDBMetric>();
        for (String stat : stats){
            if (!config.include(producerId, stat, interval))
                continue;
             metrics.addAll(helper.convert(target, stat, tags));
        }
        if (metrics.isEmpty()){
            return;
        }

        try {
            HttpPost httpPost = new HttpPost(config.getUrl());
            StringEntity entity = new StringEntity(helper.toJson(metrics));
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpClient client = HttpClients.createDefault();
            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200 || response.getStatusLine().getStatusCode() != 204){
                log.error("TSDB storage: failed to process snapshot", response);
            }
        } catch (Exception e) {
            log.error("TSDB storage: failed to process snapshot", e.getMessage());
        }



    }
}
