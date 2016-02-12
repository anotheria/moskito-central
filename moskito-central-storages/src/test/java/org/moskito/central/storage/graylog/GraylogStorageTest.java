package org.moskito.central.storage.graylog;

import org.junit.Ignore;
import org.junit.Test;
import org.moskito.central.Snapshot;
import org.moskito.central.storage.common.TestData;

/**
 * Created by Roman Stetsiuk on 2/1/16.
 */
@Ignore
public class GraylogStorageTest {

    @Test
    public void testGraylogStorage() throws Exception {

        GraylogStorage storage = new GraylogStorage();
        storage.configure("moskito-graylog");

        for (Snapshot snapshot : TestData.TEST_SNAPSHOTS) {
            storage.processSnapshot(snapshot);
        }
    }
}
