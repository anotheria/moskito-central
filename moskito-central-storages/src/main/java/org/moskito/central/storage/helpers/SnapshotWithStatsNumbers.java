package org.moskito.central.storage.helpers;

import org.moskito.central.Snapshot;
import org.moskito.central.SnapshotMetaData;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Roman Stetsiuk on 2/3/16.
 */
public class SnapshotWithStatsNumbers implements Serializable {
    private static final long serialVersionUID = 5908354352999422349L;

    /**
     * The metadata. The metadata contains data about the snapshot like
     * producerId or timestamp.
     */
    @XmlElement(name = "snshmd")
    private SnapshotMetaData metaData;

    /**
     * Stat values.
     */
    @XmlJavaTypeAdapter(MapAdapter.class)
    private Map<String, Map<String, Double>> stats = new HashMap<>();

    public SnapshotWithStatsNumbers() {}

    public SnapshotWithStatsNumbers(Snapshot snapshot) {
        this.metaData = snapshot.getMetaData();
        this.stats = convertStats(snapshot.getStats());
    }

    private Map<String, Map<String, Double>> convertStats(Map<String, Map<String, String>> stats) {
        Map<String, Map<String, Double>> res = new HashMap<>();

        for (String s : stats.keySet()) {
            res.put(s, convertStatsValueMap(stats.get(s)));
        }
        return res;
    }

    private Map<String, Double> convertStatsValueMap(Map<String, String> stringStringMap) {
        Map<String, Double> res = new HashMap<>();

        for (String s : stringStringMap.keySet()) {
            if (stringStringMap.get(s).equals("NaN"))
                res.put(s, 0.0);
            else
                res.put(s, Double.parseDouble(stringStringMap.get(s)));
        }
        return res;
    }

    public SnapshotMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(SnapshotMetaData metaData) {
        this.metaData = metaData;
    }

    /**
     * Adds snapshot data to the stats map.
     *
     * @param name
     * @param values
     */
    public void addSnapshotData(String name, Map<String, Double> values) {
        stats.put(name, values);
    }


    @Override
    public String toString() {
        return "Snapshot [metaData=" + metaData + ", stats=" + stats + "]";
    }

    /**
     * Gets statistics by specified {@link String} key.
     *
     * @param stat
     * @return {@link Map}
     */
    public Map<String, Double> getStatistics(String stat) {
        return stats.get(stat);
    }

    public Set<Map.Entry<String, Map<String, Double>>> getEntrySet() {
        return stats.entrySet();
    }

    public Set<String> getKeySet() {
        return stats.keySet();
    }

    /**
     * Gets all statistics.
     *
     * @return {@link Map}
     */
    public Map<String, Map<String, Double>> getStats() {
        if (stats == null) {
            stats = new HashMap<>();
        }
        return stats;
    }

    public void setStats(Map<String, Map<String, Double>> stats) {
        this.stats = stats;
    }

    public static class MapAdapter extends org.moskito.central.MapAdapter<String, Map<String, String>> {}

}
