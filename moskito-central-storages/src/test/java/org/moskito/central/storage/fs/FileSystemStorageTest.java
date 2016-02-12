package org.moskito.central.storage.fs;

import org.junit.Test;
import org.moskito.central.Snapshot;
import org.moskito.central.storage.common.TestData;

public class FileSystemStorageTest {

    @Test
    public void testJsonStorage() {
        FileSystemStorage fileSystemStorage = new FileSystemStorage();
        fileSystemStorage.configure("moskito-fs-new");

        for (Snapshot snapshot : TestData.TEST_SNAPSHOTS) {
            fileSystemStorage.processSnapshot(snapshot);
        }
    }
}