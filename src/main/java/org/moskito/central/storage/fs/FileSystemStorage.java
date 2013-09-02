package org.moskito.central.storage.fs;

import org.configureme.ConfigurationManager;
import org.moskito.central.Snapshot;
import org.moskito.central.storage.SnapshotSerializer;
import org.moskito.central.storage.Storage;
import org.moskito.central.storage.StorageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * File system storage implementation.
 * 
 * @author lrosenberg
 * @since 22.03.13 14:14
 */
public class FileSystemStorage implements Storage {

	/**
	 * Serializer instance.
	 */
	private SnapshotSerializer serializer;

	/**
	 * Storage config.
	 */
	private FileSystemStorageConfig config;

	/**
	 * Logger instance.
	 */
	private static Logger log = LoggerFactory.getLogger(FileSystemStorage.class);

	/**
	 * Default constructor.
	 */
	public FileSystemStorage() {
	}

	@Override
	public void configure(String configurationName) {
		config = new FileSystemStorageConfig();
		if (configurationName == null)
			return;
		try {
			ConfigurationManager.INSTANCE.configureAs(config, configurationName);
		} catch (IllegalArgumentException e) {
			log.warn("Couldn't configure FileSystemStorage with " + configurationName + " , working with default values");
		}

		try {
			serializer = SnapshotSerializer.class.cast(Class.forName(config.getSerializer()).newInstance());
		} catch (Exception e) {
			log.error("can't instantiate serializer " + serializer + " of class " + config.getSerializer(), e);
		}
	}

	@Override
	public void processSnapshot(Snapshot target) {

		if (!config.include(target.getMetaData().getProducerId(), target.getMetaData().getIntervalName())) {
			return;
		}

		if (serializer == null) {
			log.warn("can't serialize snapshots, ignoring " + target);
			return;
		}

		byte[] data = serializer.serialize(target);
		// System.out.println("Serialized: "+new String(data));

		String path = StorageUtils.convertPathPattern(config.getPattern(), target);

		FileOutputStream fOut = null;
		String dirName = path.substring(0, path.lastIndexOf('/'));
		File f = new File(dirName);
		f.mkdirs();
		try {
			fOut = new FileOutputStream(path);
			fOut.write(data);
			fOut.flush();
		} catch (IOException e) {
			log.error("can't serialize snapshot " + target, e);
		} finally {
			if (fOut != null) {
				try {
					fOut.close();
				} catch (IOException ignored) {
				}
			}
		}
	}
}
