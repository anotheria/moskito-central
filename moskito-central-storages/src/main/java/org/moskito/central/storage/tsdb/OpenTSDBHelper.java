package org.moskito.central.storage.tsdb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.anotheria.util.StringUtils;
import org.moskito.central.Snapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * OpenTSDB helper.
 *
 * @author esmakula
 * @since 10.10.13
 */
public class OpenTSDBHelper {
    /**
     * Tag interval.
     */
    private static final String TAG_INTERVAL = "intervalName";
    /**
     * Tag host.
     */
    private static final String TAG_HOST = "hostName";
    /**
     * Tag component.
     */
    private static final String TAG_COMPONENT = "componentName";


    /**
     * Logger instance.
     */
    private static Logger log = LoggerFactory.getLogger(OpenTSDBHelper.class);


	/**
	 * Cash for value names by producer. This allows to faster process snapshot,
	 * without the need to resort each time.
	 */
	private ConcurrentMap<String, List<String>> cachedValueNames = new ConcurrentHashMap<>();

    /**
     * Gson instance.
     */
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();


    /**
     * Convert to {@link OpenTSDBMetric} collection.
     *
     * @param snapshot snapshot.
     * @param stat stat name.
     * @param tags tags.
     * @return collection of {@link OpenTSDBMetric}.
     */
	public List<OpenTSDBMetric> convert(Snapshot snapshot, String stat, Map<String, String> tags) {
		List<String> valueNames = getValueNames(snapshot);
		Map<String, String> data = snapshot.getStatistics(stat);
		if (data == null)
			return null;

		List<OpenTSDBMetric> metrics = new ArrayList<>();
		long creationTimestamp = snapshot.getMetaData().getCreationTimestamp();
        NumberFormat nf = NumberFormat.getInstance();
		for (String s : valueNames) {
			Number value;
            String valueString = data.get(s);
            if (StringUtils.isEmpty(valueString)){
                log.info(String.format("Skipped, empty value for stat %s.%s.%s",
                        snapshot.getMetaData().getProducerId(), stat, s));
                continue;
            }
            try{
                value = nf.parse(valueString);
            } catch (ParseException e){
                log.info(String.format("Skipped, can't parse value %s for stat %s.%s.%s",
                        valueString, snapshot.getMetaData().getProducerId(), stat, s));
                continue;
            }
            OpenTSDBMetric metric = new OpenTSDBMetric();
            metric.setMetric(String.format("%s.%s.%s", snapshot.getMetaData().getProducerId(), stat, s));
            metric.setValue(value);
            metric.setTimestamp(creationTimestamp);
            metric.setTags(tags);
            metrics.add(metric);
		}
		return metrics;
	}

    /**
     * Get metric tags from snapshot.
     *
     * @param snapshot snapshot.
     * @return tags map.
     */
    public Map<String, String> getTags(Snapshot snapshot) {
        Map<String, String> tags = new HashMap<>();
        tags.put(TAG_HOST, snapshot.getMetaData().getHostName());
        tags.put(TAG_COMPONENT, snapshot.getMetaData().getComponentName());
        tags.put(TAG_INTERVAL, snapshot.getMetaData().getIntervalName());
        return tags;
    }

    /**
     * To json.
     *
     * @param metrics collection of {@link OpenTSDBMetric}.
     * @return json.
     */
    public String toJson(List<OpenTSDBMetric> metrics){
        return gson.toJson(metrics);
    }

    /**
     * Get stat value names.
     *
     * @param snapshot snapshot.
     * @return collection of names.
     */
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
		List<String> old = cachedValueNames.putIfAbsent(snapshot.getMetaData().getProducerId(), valueNames);
		return old == null ? valueNames : old;
	}

}
