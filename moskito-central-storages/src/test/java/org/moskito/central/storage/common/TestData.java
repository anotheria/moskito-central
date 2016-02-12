package org.moskito.central.storage.common;

import org.moskito.central.Snapshot;
import org.moskito.central.SnapshotMetaData;

import java.util.*;

/**
 * @author bvanchuhov
 */
public final class TestData {

    private TestData() {
    }

    public static List<Snapshot> TEST_SNAPSHOTS = createTestSnapshots();

    private static List<Snapshot> createTestSnapshots() {
        return Collections.unmodifiableList(Arrays.asList(
                createSnapshot1(),
                createSnapshot2()
        ));
    }

    private static Snapshot createSnapshot1() {
        Snapshot snapshot = new Snapshot();

        SnapshotMetaData metaData = new SnapshotMetaData()
                .setHostName("localhost")
                .setProducerId("SessionCount")
                .setCategory("TestAPI")
                .setSubsystem("Test")
                .setArrivalTimestamp(System.currentTimeMillis())
                .setCreationTimestamp(System.currentTimeMillis());
        snapshot.setMetaData(metaData);

        Map<String, String> cumulated = new HashMap<>();
        cumulated.put("Number", "1");
        cumulated.put("Volume", "NaN");
        snapshot.addSnapshotData("cumulated", cumulated);

        Map<String, String> brioche = new HashMap<>();
        brioche.put("Number", "2");
        brioche.put("Volume", "3");
        snapshot.addSnapshotData("brioche", brioche);

        return snapshot;
    }

    private static Snapshot createSnapshot2() {
        Snapshot snapshot = new Snapshot();

        SnapshotMetaData snmd2 = new SnapshotMetaData()
                .setProducerId("MafFilter")
                .setCategory("TestBiz")
                .setSubsystem("test")
                .setHostName("localhost")
                .setArrivalTimestamp(System.currentTimeMillis())
                .setCreationTimestamp(System.currentTimeMillis());
        snapshot.setMetaData(snmd2);

        HashMap<String, String> cumulated2 = new HashMap<>();
        cumulated2.put("Number", "11");
        cumulated2.put("Volume", "NaN");
        snapshot.addSnapshotData("cumulated", cumulated2);

        HashMap<String, String> brioche2 = new HashMap<>();
        brioche2.put("Number", "11");
        brioche2.put("Volume", "12");
        snapshot.addSnapshotData("brioche", brioche2);

        return snapshot;
    }
}
