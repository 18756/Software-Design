import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LRUCacheByLinkedHashMap<K, V> implements LRUCache<K, V> {
    private final LinkedHashMap<K, V> map;
    private final int maxSize;
    private final V defaultValue;

    public LRUCacheByLinkedHashMap(int maxSize, V defaultValue) {
        this.maxSize = maxSize;
        this.defaultValue = defaultValue;
        map = new LinkedHashMap<>();
    }

    @Override
    public void put(K key, V value) {
        if (map.containsKey(key)) {
            map.remove(key);
        } else if (map.size() == maxSize) {
            map.remove(map.entrySet().iterator().next().getKey());
        }
        map.put(key, value);
    }

    @Override
    public V get(K key) {
        if (map.containsKey(key)) {
            V value = map.get(key);
            map.remove(key);
            map.put(key, value);
            return value;
        } else {
            if (map.size() == maxSize) {
                map.remove(map.entrySet().iterator().next().getKey());
            }
            map.put(key, defaultValue);
            return defaultValue;
        }
    }

    @Override
    public Set<K> getAllKeys() {
        return map.keySet();
    }

    @Override
    public Map<K, V> getMap() {
        return map;
    }
}
