package org.moskito.central.storage.rabbitmq;

import org.moskito.central.HashMapAdapter;
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
public class SnapshotWithStatsNumbers implements Serializable{
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
    private HashMap<String, HashMap<String, Double>> stats = new HashMap<>();

    public SnapshotWithStatsNumbers() {}

    public SnapshotWithStatsNumbers(Snapshot snapshot) {
        this.metaData = snapshot.getMetaData();
        this.stats = convertStats(snapshot.getStats());
    }

    private HashMap<String, HashMap<String, Double>> convertStats(HashMap<String, HashMap<String, String>> stats) {
        HashMap<String, HashMap<String, Double>> res = new HashMap<>();

        for (String s : stats.keySet()) {
            res.put(s, convertStatsValueMap(stats.get(s)));
        }
        return res;
    }

    private HashMap<String, Double> convertStatsValueMap(HashMap<String, String> stringStringHashMap) {
        HashMap<String, Double> res = new HashMap<>();

        for (String s : stringStringHashMap.keySet()) {
            if (stringStringHashMap.get(s).equals("NaN"))
                res.put(s, 0.0);
            else
                res.put(s, Double.parseDouble(stringStringHashMap.get(s)));
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
    public void addSnapshotData(String name, HashMap<String, Double> values) {
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
     * @return {@link Map <String, String>}
     */
    public Map<String, Double> getStatistics(String stat) {
        return stats.get(stat);
    }

    public Set<Map.Entry<String, HashMap<String, Double>>> getEntrySet() {
        return stats.entrySet();
    }

    public Set<String> getKeySet() {
        return stats.keySet();
    }

    /**
     * Gets all statistics.
     *
     * @return {@link HashMap<String, HashMap<String, Integer>>}
     */
    public HashMap<String, HashMap<String, Double>> getStats() {
        if (stats == null) {
            stats = new HashMap<String, HashMap<String, Double>>();
        }
        return stats;
    }

    public void setStats(HashMap<String, HashMap<String, Double>> stats) {
        this.stats = stats;
    }

    public static class MapAdapter extends HashMapAdapter<String, HashMap<String, String>> {}

}
