package org.moskito.central.storage.fs;

import org.junit.Test;
import org.moskito.central.Snapshot;
import org.moskito.central.SnapshotMetaData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author andriiskrypnyk
 */
public class XMLSerilizerTest {

    @Test
    public void testSerializer() {

        FileSystemStorage storage = new FileSystemStorage();
        storage.configure("moskito-fs");
        Snapshot sn = new Snapshot();

        SnapshotMetaData snmd = new SnapshotMetaData();
        snmd.setCategory("test1");
        snmd.setProducerId("SessionCount");
        snmd.setArrivalTimestamp(System.currentTimeMillis());
        snmd.setHostName("localhost");
        snmd.setCreationTimestamp(System.currentTimeMillis());
        sn.setMetaData(snmd);
        HashMap<String, String> cumulated = new HashMap<String, String>();
        cumulated.put("Number", "2");
        cumulated.put("Volume", "7509");
        sn.addSnapshotData("cumulated", cumulated);
        HashMap<String, String> brioche=new HashMap<String, String>();
        brioche.put("Number","2");
        brioche.put("Volume","7509");
        sn.addSnapshotData("brioche",brioche);


        storage.processSnapshot(sn);


    }


}
