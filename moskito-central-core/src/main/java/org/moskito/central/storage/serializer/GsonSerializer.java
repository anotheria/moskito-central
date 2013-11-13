package org.moskito.central.storage.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.moskito.central.Snapshot;
import org.moskito.central.storage.SnapshotSerializer;

/**
 * {@link Gson} serializer.
 * 
 * @author lrosenberg
 * @since 22.03.13 15:00
 */
public class GsonSerializer implements SnapshotSerializer {

	/**
	 * {@link Gson} instance.
	 */
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public byte[] serialize(Snapshot snapshot) {
		String jsonOutput = gson.toJson(snapshot);
		return jsonOutput.getBytes();
	}
}
