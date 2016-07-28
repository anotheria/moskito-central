package org.moskito.central;

import org.configureme.ConfigurationManager;
import org.moskito.central.config.Configuration;
import org.moskito.central.config.StorageConfigEntry;
import org.moskito.central.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Main class of the central.
 *
 * @author lrosenberg
 * @since 15.03.13 23:10
 */
public class Central {

	/**
	 * Configured and therefore active storages.
	 */
	private ConcurrentMap<String, Storage> storages = new ConcurrentHashMap<>();
	/**
	 * List used for faster iteration for snapshot delivery.
	 */
	private Collection<Storage> cachedList = new CopyOnWriteArrayList<>();

	/**
	 * Configuration.
	 */
	private Configuration configuration;

	/**
	 * Log.
	 */
	private static Logger log = LoggerFactory.getLogger(Central.class);

	/**
	 * Returns the singleton instance.
	 * @return
	 */
	public static Central getInstance(){
		return CentralInstanceHolder.instance;
	}

	/**
	 * Returns a specially configured instance. For unit tests.
	 * @param config
	 * @return
	 */
	public static Central getConfiguredInstance(Configuration config){
		Central instance = new Central();
        instance.configuration = config;
        instance.setup();
		return instance;
	}

	/**
	 * This method should only be called once at a time. Therefore we do not synchronize it, cause it should only be
	 * called on instantiation or in a test.
	 */
	private void setup(){
		storages.clear();
		cachedList.clear();
		for (StorageConfigEntry storageConfigEntry : configuration.getStorages()){
			try{
				Storage storage = Storage.class.cast(Class.forName(storageConfigEntry.getClazz()).newInstance());
				try{
					storage.configure(storageConfigEntry.getConfigName());
					storages.put(storageConfigEntry.getName(), storage);
					cachedList.add(storage);
				}catch(Exception e){
					log.warn("Storage "+storage+" for "+storageConfigEntry+" couldn't be configured properly.");
				}

			}catch(ClassNotFoundException | IllegalAccessException | InstantiationException cnf){
				log.warn("Couldn't instantiate StorageConfigEntry "+storageConfigEntry+" due ",cnf);
			}

        }
	}


	public void processIncomingSnapshot(Snapshot snapshot){
		for (Storage s : cachedList){
			try{
				s.processSnapshot(snapshot);
			}catch(Exception any){
				log.warn("Exception caught during snapshot processing in storage "+s+", snapshot: "+snapshot, any);
			}
		}
	}

	private void setConfiguration(Configuration aConfiguration){
		configuration = aConfiguration;
	}


	/**
	 * Central singleton instance holder.
	 */
	private static class CentralInstanceHolder{
		/**
		 * Helds the Central singleton instance.
 		 */
		static final Central instance;
		static{
			instance = new Central();
			Configuration configuration = new Configuration();
			try{
				ConfigurationManager.INSTANCE.configure(configuration);
			}catch(IllegalArgumentException e){
				//not found
			}
			instance.setConfiguration(configuration);
			instance.setup();

		}

	}

	/*testing scope*/ Configuration getConfiguration(){
		return configuration;
	}



}
