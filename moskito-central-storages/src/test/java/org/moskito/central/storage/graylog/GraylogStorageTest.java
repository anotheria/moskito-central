package org.moskito.central.storage.graylog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.moskito.central.Snapshot;
import org.moskito.central.SnapshotMetaData;

import java.util.HashMap;

/**
 * Created by Roman Stetsiuk on 2/1/16.
 */
public class GraylogStorageTest {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void elasticsearchTest() throws Exception {

        GraylogStorage storage=new GraylogStorage();
        storage.configure("moskito-graylog");

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
        cumulated.put("Volume", "NaN");
        sn.addSnapshotData("cumulated", cumulated);

        HashMap<String, String> brioche = new HashMap<String, String>();
        brioche.put("Number", "777");
        brioche.put("Volume", "7509");
        sn.addSnapshotData("brioche", brioche);

        storage.processSnapshot(sn);
    }
}
