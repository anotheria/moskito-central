package org.moskito.central;

import org.junit.Test;
import org.moskito.central.config.Configuration;
import org.moskito.central.config.StorageConfigEntry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConfigurationTest {
	@Test
	public void testConfiguration(){
		Configuration config = Central.getInstance().getConfiguration();
		StorageConfigEntry[] entries = config.getStorages();
		assertNotNull(entries);
		assertEquals(3, entries.length);

		assertEquals("json-file", entries[0].getName());
		assertEquals("csv-file", entries[1].getName());
		assertEquals("psql-db", entries[2].getName());

	}
}
