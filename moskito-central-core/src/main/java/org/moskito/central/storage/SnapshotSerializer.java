package org.moskito.central.storage;

import org.moskito.central.Snapshot;

/**
 * Defines serializers for snapshots.
 *
 * @author lrosenberg
 * @since 22.03.13 14:07
 */
public interface SnapshotSerializer {
	/**
	 * Creates a serialized version of a snapshot and returns byte version.
	 * @param snapshot snapshot to serialize.
	 * @return
	 */
	byte[] serialize(Snapshot snapshot);
}
