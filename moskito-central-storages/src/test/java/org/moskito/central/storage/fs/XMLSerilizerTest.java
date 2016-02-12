package org.moskito.central.storage.fs;

import org.junit.Test;
import org.moskito.central.storage.common.TestData;

/**
 * @author andriiskrypnyk
 */
public class XMLSerilizerTest {

    @Test
    public void testSerializer() {
        FileSystemStorage storage = new FileSystemStorage();
        storage.configure("moskito-xml");

        storage.processSnapshot(TestData.TEST_SNAPSHOTS.get(0));
    }
}
