package org.moskito.central;

import org.moskito.central.Central;
import org.moskito.central.config.Configuration;
import org.moskito.central.config.StorageConfigEntry;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 22.03.13 11:53
 */
public class ConfigurationTest {
	@Test
	public void testTestConfiguration(){
		Configuration config = Central.getInstance().getConfiguration();
		StorageConfigEntry[] entries = config.getStorages();
		assertNotNull(entries);
		//System.out.println(Arrays.toString(entries));
	}
}
