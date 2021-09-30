import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface LRUCache<K, V> {
    public void put(K key, V value);

    public V get(K key);

    public Set<K> getAllKeys();

    public Map<K, V> getMap();
}
