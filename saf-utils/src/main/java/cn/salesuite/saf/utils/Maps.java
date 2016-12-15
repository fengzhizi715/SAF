/**
 * 
 */
package cn.salesuite.saf.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Tony Shen
 *
 */
public class Maps {

	/**
	 * @param map
	 * @return
	 */
	public static <K, V> K key(Map<K, V> map) {
		if (map != null) {
			Iterator<Entry<K, V>> iterator = map.entrySet().iterator();
			if (iterator.hasNext()) {
				return iterator.next().getKey();
			}
		}

		return null;
	}
	
	/**
	 * @param map
	 * @return
	 */
	public static <K, V> V value(Map<K, V> map) {
		if (map != null) {
			Iterator<Entry<K, V>> iterator = map.entrySet().iterator();
			if (iterator.hasNext()) {
				return iterator.next().getValue();
			}
		}

		return null;
	}
}
