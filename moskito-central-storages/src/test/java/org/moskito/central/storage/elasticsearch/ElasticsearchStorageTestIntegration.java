package org.moskito.central.storage.elasticsearch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Test;
import org.moskito.central.Snapshot;
import org.moskito.central.SnapshotMetaData;

import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Created by andriiskrypnyk on 4/22/15.
 */
public class ElasticsearchStorageTestIntegration {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void elasticsearchTest() throws Exception {

        ElasticsearchStorage storage=new ElasticsearchStorage();
        storage.configure("moskito-elasticsearch");

        Snapshot sn = new Snapshot();

        SnapshotMetaData snmd = new SnapshotMetaData();
        snmd.setCategory("test1");
        snmd.setProducerId("SessionCount");
        snmd.setArrivalTimestamp(System.currentTimeMillis());
        snmd.setHostName("localhost");
        snmd.setCreationTimestamp(System.currentTimeMillis());
        sn.setMetaData(snmd);
        HashMap<String, String> cumulated = new HashMap<String, String>();
        cumulated.put("Number", "888");
        cumulated.put("Volume", "7509");
        sn.addSnapshotData("cumulated", cumulated);
        HashMap<String, String> brioche = new HashMap<String, String>();
        brioche.put("Number", "777");
        brioche.put("Volume", "7509");
        sn.addSnapshotData("brioche", brioche);

        storage.processSnapshot(sn);




    }
}

