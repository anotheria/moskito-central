package org.moskito.central.storage.mongo;


import org.junit.Test;
import org.moskito.central.Snapshot;
import org.moskito.central.SnapshotMetaData;

import java.util.HashMap;

/**
 * Created by andriiskrypnyk on 4/20/15.
 */
public class MongoDBStorageTest {

    @Test
    public void mongoTest() {
        MongoDBStorage storage = new MongoDBStorage();
        storage.configure("moskito-mongoDB");

        Snapshot sn = new Snapshot();

        SnapshotMetaData snmd = new SnapshotMetaData();
        snmd.setCategory("test1");
        snmd.setProducerId("SessionCount");
        snmd.setArrivalTimestamp(System.currentTimeMillis());
        snmd.setHostName("localhost");
        snmd.setCreationTimestamp(System.currentTimeMillis());
        sn.setMetaData(snmd);
        HashMap<String, String> cumulated = new HashMap<String, String>();
        cumulated.put("Number", "7");
        cumulated.put("Volume", "7509");
        sn.addSnapshotData("cumulated", cumulated);
        HashMap<String, String> brioche = new HashMap<String, String>();
        brioche.put("Number", "2");
        brioche.put("Volume", "7509");
        sn.addSnapshotData("brioche", brioche);

        storage.processSnapshot(sn);

    }


}
