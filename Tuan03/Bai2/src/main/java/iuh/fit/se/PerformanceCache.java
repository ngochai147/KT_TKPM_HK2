package iuh.fit.se;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * PerformanceCache - Cache LRU đơn giản để tăng hiệu suất
 */
public class PerformanceCache<K, V> {
    private final Map<K, V> cache;

    public PerformanceCache(int maxSize) {
        this.cache = new LinkedHashMap<>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxSize;
            }
        };
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public V get(K key) {
        return cache.get(key);
    }

    public int size() {
        return cache.size();
    }
}

