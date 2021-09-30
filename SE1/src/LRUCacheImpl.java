import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LRUCacheImpl<K, V> implements LRUCache<K, V> {
    private final int maxSize;
    private Node first = null;
    private Node last = null;
    private final Map<K, Node> map;
    private final V defaultValue;

    public LRUCacheImpl(int maxSize, V defaultValue) {
        Assertions.assertTrue(maxSize > 0);
        this.maxSize = maxSize;
        this.defaultValue = defaultValue;
        this.map = new HashMap<>();
    }

    public void put(K key, V value) {
        Assertions.assertTrue(map.size() <= maxSize);
        int oldSize = map.size();
        boolean alreadyContains = map.containsKey(key);

        doPut(key, value);

        if (alreadyContains) {
            Assertions.assertEquals(oldSize, map.size());
        } else {
            Assertions.assertEquals(Math.min(maxSize, oldSize + 1), map.size());
        }
        Assertions.assertEquals(value, map.get(key).value);
    }

    public V get(K key) {
        Assertions.assertTrue(map.size() <= maxSize);
        int oldSize = map.size();
        boolean alreadyContains = map.containsKey(key);

        V value = doGet(key);

        if (alreadyContains) {
            Assertions.assertEquals(oldSize, map.size());
        } else {
            Assertions.assertEquals(Math.min(maxSize, oldSize + 1), map.size());
            Assertions.assertEquals(defaultValue, value);
        }
        Assertions.assertEquals(value, map.get(key).value);
        return value;
    }

    public Set<K> getAllKeys() {
        return map.keySet();
    }

    public Map<K, V> getMap() {
        Map<K, V> res = new HashMap<>();
        for (K key : map.keySet()) {
            res.put(key, map.get(key).value);
        }
        return res;
    }

    private void doPut(K key, V value) {
        Node node = getNodeByKey(key);
        node.value = value;
    }

    private V doGet(K key) {
        return getNodeByKey(key).value;
    }

    private Node getNodeByKey(K key) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            makeFresh(node);
            return node;
        }
        Node newNode = new Node(key, defaultValue);
        insertNode(key, newNode);
        return newNode;
    }

    private void makeFresh(Node node) {
        Node prev = node.prev;
        Node next = node.next;
        if (next != null) {
            if (prev == null) {
                first = next;
            } else {
                prev.next = next;
            }
            next.prev = prev;

            node.prev = last;
            node.next = null;
            last.next = node;
            last = node;
        }
    }

    private void insertNode(K key, Node newNode) {
        if (map.size() == maxSize) {
            removeOldestNode();
        }
        map.put(key, newNode);
        if (first == null) {
            first = newNode;
            last = newNode;
        } else {
            newNode.prev = last;
            last.next = newNode;
            last = newNode;
        }
    }

    private void removeOldestNode() {
        if (first != null) {
            map.remove(first.key);
            first = first.next;
            if (first != null) {
                first.prev = null;
            } else {
                last = null;
            }
        }
    }

    class Node {
        K key;
        V value;
        Node prev;
        Node next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
