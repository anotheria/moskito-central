package org.moskito.central.storage.psql;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.Ignore;
import org.junit.Test;
import org.moskito.central.Snapshot;
import org.moskito.central.SnapshotMetaData;

@Ignore
public class PSQLStorageTest {
	
	@Test
	public void test() {
		
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("postgres"));
		
		PSQLStorage storage = new PSQLStorage();
		storage.configure("moskito-psql-hibernate");
		
		Snapshot sn = new Snapshot();
		
		SnapshotMetaData snmd = new SnapshotMetaData();
		snmd.setCategory("test1");
		snmd.setProducerId("SessionCount");
		snmd.setArrivalTimestamp(System.currentTimeMillis());
		snmd.setHostName("localhost");
		snmd.setCreationTimestamp(System.currentTimeMillis());
		
		sn.setMetaData(snmd);
		
		storage.processSnapshot(sn);
	}

}
