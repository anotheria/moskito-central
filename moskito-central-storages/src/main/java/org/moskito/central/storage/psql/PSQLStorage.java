package org.moskito.central.storage.psql;

import org.configureme.ConfigurationManager;
import org.moskito.central.Snapshot;
import org.moskito.central.storage.Storage;
import org.moskito.central.storage.psql.entities.JSONStatisticsEntity;
import org.moskito.central.storage.psql.entities.SnapshotEntity;
import org.moskito.central.storage.psql.entities.StatisticsEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import java.util.HashMap;
import java.util.Map;

/**
 * PSQL snapshot storage implementation.
 * 
 * @author dagafonov
 * 
 */
public class PSQLStorage implements Storage {

	/**
	 * Persistence unit name defined in /META-INF/persistence.xml.
	 */
	private static final String PERSISTENCE_UNIT_NAME = "snapshotStorage";

	/**
	 * Logger instance.
	 */
	private static Logger log = LoggerFactory.getLogger(PSQLStorage.class);

	/**
	 * Storage config.
	 */
	private PSQLStorageConfig config;

	/**
	 * EntityManager instance.
	 */
	private EntityManagerFactory factory;

	@Override
	public void configure(String configurationName) {
		config = new PSQLStorageConfig();
		if (configurationName == null)
			return;
		try {
			ConfigurationManager.INSTANCE.configureAs(config, configurationName);
		} catch (IllegalArgumentException e) {
			log.warn("Couldn't configure PSQLStorage with " + configurationName + " , working with default values");
		}

		Map<String, String> map = new HashMap<>();
		map.put("javax.persistence.jdbc.driver", config.getDriver());
		map.put("javax.persistence.jdbc.url", config.getUrl());
		map.put("javax.persistence.jdbc.user", config.getUserName());
		map.put("javax.persistence.jdbc.password", config.getPassword());

		if (config.getHibernateDialect() != null) {
			map.put("hibernate.dialect", config.getHibernateDialect());
		}
		map.put("hibernate.hbm2ddl.auto", "update");
		map.put("hibernate.show_sql", "true");
		
		try {
			factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, map);
		} catch (PersistenceException e) {
			log.error("Persistence.createEntityManagerFactory({}, ({})", PERSISTENCE_UNIT_NAME, map, e);
		}
	}

	@Override
	public void processSnapshot(Snapshot target) {

		String producerId = target.getMetaData().getProducerId();
		String interval = target.getMetaData().getIntervalName();

		if (!config.include(producerId, interval)) {
			return;
		}

		Class<? extends StatisticsEntity> statEntityClass = config.getStatEntityClassName(target.getMetaData().getStatClassName(), producerId);
		if (statEntityClass == null) {
			statEntityClass = JSONStatisticsEntity.class;
		}

		SnapshotEntity entity = new SnapshotEntity();
		entity.setProducerId(producerId);
		entity.setCategory(target.getMetaData().getCategory());

		entity.setSubsystem(target.getMetaData().getSubsystem());
		entity.setIntervalName(interval);

		entity.setComponentName(target.getMetaData().getComponentName());
		entity.setHostName(target.getMetaData().getHostName());

		entity.setCreationTimestamp(target.getMetaData().getCreationTimestamp());

		for (String key : target.getKeySet()) {

			StatisticsEntity entityInstance = null;
			try {
				entityInstance = statEntityClass.newInstance();
			} catch (IllegalAccessException e) {
				log.error("Instance cannot be instantiated", e);
				continue;
			} catch (InstantiationException e) {
				log.error("Instance cannot be instantiated", e);
				continue;
			}

			entityInstance.setStats(target.getStatistics(key));
			entity.addStatistics(key, entityInstance);
		}

		EntityManager manager = factory.createEntityManager();
		EntityTransaction tr = manager.getTransaction();
		try {
			tr.begin();
			manager.persist(entity);
			tr.commit();
		} catch (Exception e) {
			log.error("persist failed", e);
			tr.rollback();
		}
	}

}
