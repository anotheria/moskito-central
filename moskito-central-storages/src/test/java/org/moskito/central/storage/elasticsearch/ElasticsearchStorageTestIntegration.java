package org.moskito.central.storage.elasticsearch;

import org.junit.Test;
import org.moskito.central.storage.common.TestData;

/**
 * Created by andriiskrypnyk on 4/22/15.
 */
public class ElasticsearchStorageTestIntegration {

    @Test
    public void elasticsearchTest() throws Exception {
        ElasticsearchStorage storage = new ElasticsearchStorage();
        storage.configure("moskito-elasticsearch");

        storage.processSnapshot(TestData.TEST_SNAPSHOTS.get(0));
    }
}

