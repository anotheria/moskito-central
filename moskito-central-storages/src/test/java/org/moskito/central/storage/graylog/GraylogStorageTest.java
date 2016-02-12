package org.moskito.central.storage.graylog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Ignore;
import org.junit.Test;
import org.moskito.central.Snapshot;
import org.moskito.central.SnapshotMetaData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roman Stetsiuk on 2/1/16.
 */
@Ignore
public class GraylogStorageTest {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testGraylogStorage() throws Exception {

        GraylogStorage storage = new GraylogStorage();
        storage.configure("moskito-graylog");

        Snapshot sn = new Snapshot();

        SnapshotMetaData snmd = new SnapshotMetaData();
            snmd.setCategory("test1");
            snmd.setProducerId("SessionCount");
            snmd.setArrivalTimestamp(System.currentTimeMillis());
            snmd.setHostName("localhost");
            snmd.setCreationTimestamp(System.currentTimeMillis());
        sn.setMetaData(snmd);

        Map<String, String> cumulated = new HashMap<>();
            cumulated.put("Number", "888");
            cumulated.put("Volume", "NaN");
        sn.addSnapshotData("cumulated", cumulated);

        Map<String, String> brioche = new HashMap<>();
            brioche.put("Number", "777");
            brioche.put("Volume", "7509");
        sn.addSnapshotData("brioche", brioche);

        storage.processSnapshot(sn);


        Snapshot sn2 = new Snapshot();

        SnapshotMetaData snmd2 = new SnapshotMetaData();
        snmd2.setCategory("test1");
        snmd2.setProducerId("MafFilter");
        snmd2.setArrivalTimestamp(System.currentTimeMillis());
        snmd2.setHostName("localhost");
        snmd2.setCreationTimestamp(System.currentTimeMillis());
        sn2.setMetaData(snmd2);

        HashMap<String, String> cumulated2 = new HashMap<>();
        cumulated2.put("Number", "888");
        cumulated2.put("Volume", "NaN");
        sn2.addSnapshotData("cumulated", cumulated);

        HashMap<String, String> brioche2 = new HashMap<>();
        brioche2.put("Number", "777");
        brioche2.put("Volume", "7509");
        sn2.addSnapshotData("brioche", brioche2);

        storage.processSnapshot(sn2);

    }
}
