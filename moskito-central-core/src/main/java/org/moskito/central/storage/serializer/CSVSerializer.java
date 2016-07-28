package org.moskito.central.storage.serializer;

import net.anotheria.util.NumberUtils;
import org.moskito.central.Snapshot;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Special serializer for comma-separater-value list files.
 *
 * @author lrosenberg
 * @since 24.03.13 23:06
 */
public class CSVSerializer {

	/**
	 * Cash for value names by producer. This allows to faster process snapshot,
	 * without the need to resort each time.
	 */
	private ConcurrentMap<String, List<String>> cachedValueNames = new ConcurrentHashMap<>();

	public byte[] serialize(Snapshot snapshot, String stat) {
		List<String> valueNames = getValueNames(snapshot);
		Map<String, String> data = snapshot.getStatistics(stat);
		if (data == null)
			return null;
		StringBuilder ret = new StringBuilder();
		long creationTimestamp = snapshot.getMetaData().getCreationTimestamp();
		for (String s : valueNames) {
			boolean special = false;
			if (ret.length() > 0)
				ret.append(';');
			if (s.equals("SnapshotTime")) {
				special = true;
				ret.append('"').append(NumberUtils.makeTimeString(creationTimestamp)).append('"');
			}
			if (s.equals("SnapshotDate")) {
				special = true;
				ret.append('"').append(NumberUtils.makeDigitalDateStringLong(creationTimestamp)).append('"');
			}
			if (s.equals("SnapshotTimestamp")) {
				special = true;
				ret.append('"').append(NumberUtils.makeISO8601TimestampString(creationTimestamp)).append('"');

			}
			if (!special)
				ret.append('"').append(data.get(s)).append('"');
		}
		try {
			return ret.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError("UTF-8 not supported");
		}
	}

	public byte[] getHeader(Snapshot snapshot) {
		List<String> valueNames = getValueNames(snapshot);
		StringBuilder ret = new StringBuilder();
		for (String s : valueNames) {
			if (ret.length() > 0)
				ret.append(';');
			ret.append('"').append(s).append('"');
		}
		try {
			return ret.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError("UTF-8 not supported");
		}
	}

	private List<String> getValueNames(Snapshot snapshot) {
		ArrayList<String> valueNames = ((ArrayList<String>) cachedValueNames.get(snapshot.getMetaData().getProducerId()));
		if (valueNames != null)
			return valueNames;
		valueNames = new ArrayList<>();
		Set<Map.Entry<String, Map<String, String>>> entries = snapshot.getEntrySet();
		if (entries.isEmpty()) {
			List<String> old = cachedValueNames.putIfAbsent(snapshot.getMetaData().getProducerId(), valueNames);
			return old == null ? valueNames : old;
		}

		Map<String, String> oneStat = entries.iterator().next().getValue();
		valueNames.addAll(oneStat.keySet());
		Collections.sort(valueNames);
		// add special values
		valueNames.add(0, "SnapshotTime");
		valueNames.add(0, "SnapshotDate");
		valueNames.add(0, "SnapshotTimestamp");
		List<String> old = cachedValueNames.putIfAbsent(snapshot.getMetaData().getProducerId(), valueNames);
		return old == null ? valueNames : old;
	}
}
