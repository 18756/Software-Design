import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {
    private LRUCacheImpl<Integer, Integer> lruCache;
    private LRUCacheByLinkedHashMap<Integer, Integer> lruCacheByLinkedHashMap;
    private final int DEFAULT_VALUE = 0;
    private final Random random = new Random();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        lruCache = null;
    }

    @Test
    void OneSizeCacheTest() {
        createLRUCache(1);
        puts(2,2, 1,1);
        assertEquals(1, lruCache.get(1));
        assertEquals(set(1), lruCache.getAllKeys());
        puts(2,2);
        assertEquals(2, lruCache.get(2));
        assertEquals(set(2), lruCache.getAllKeys());
        assertEquals(DEFAULT_VALUE, lruCache.get(1));
        assertEquals(set(1), lruCache.getAllKeys());
    }

    @Test
    void TwoSizeCacheTest() {
        createLRUCache(2);
        puts(1,1, 2,2);
        assertEquals(1, lruCache.get(1));
        assertEquals(2, lruCache.get(2));
        assertEquals(set(1, 2), lruCache.getAllKeys());
        puts(3,3);
        assertEquals(2, lruCache.get(2));
        assertEquals(set(2, 3), lruCache.getAllKeys());
        assertEquals(DEFAULT_VALUE, lruCache.get(1));
        assertEquals(set(1, 2), lruCache.getAllKeys());
        assertEquals(2, lruCache.get(2));
        assertEquals(set(1, 2), lruCache.getAllKeys());
    }

    @Test
    void ThreeSizeCacheTest() {
        createLRUCache(3);
        puts(1,1, 2,2, 3,3, 4,4);
        assertEquals(set(2, 3, 4), lruCache.getAllKeys());
        assertEquals(DEFAULT_VALUE, lruCache.get(1));
        assertEquals(set(1, 3, 4), lruCache.getAllKeys());
        assertEquals(DEFAULT_VALUE, lruCache.get(2));
        assertEquals(set(1, 2, 4), lruCache.getAllKeys());
        gets(1);
        puts(5,5, 6,6);
        assertEquals(set(1, 5, 6), lruCache.getAllKeys());
    }

    @Test
    void randomTests() {
        randomTest(100, 10, 1_000, 1);
        randomTest(100, 100, 10_000, 10);
        randomTest(50, 500, 100_000, 100);
        randomTest(10, 1_000, 1_000_000, 1_000);
        randomTest(10, 1_000_000, 100_000, 1_000);
    }

    void randomTest(int tests, int maxSize, int operations, int checkPeriod) {
        for (int i = 0; i < tests; i++) {
            createLRUCache(maxSize);
            for (int j = 0; j < operations; j++) {
                int key = random.nextInt(2 * maxSize);
                if (random.nextBoolean()) {
                    gets(key);
                } else {
                    int value = random.nextInt();
                    puts(key, value);
                }
                if (j % checkPeriod == 0) {
                    Assertions.assertEquals(lruCacheByLinkedHashMap.getMap(), lruCache.getMap());
                }
            }
        }
    }


    void createLRUCache(int maxSize) {
        lruCache = new LRUCacheImpl<>(maxSize, DEFAULT_VALUE);
        lruCacheByLinkedHashMap = new LRUCacheByLinkedHashMap<>(maxSize, DEFAULT_VALUE);
    }

    void puts(int... pairs) {
        assertEquals(0, pairs.length % 2, "provide even amount of elements for pairs");
        for (int i = 0; i < pairs.length; i += 2) {
            lruCache.put(pairs[i], pairs[i + 1]);
            lruCacheByLinkedHashMap.put(pairs[i], pairs[i + 1]);
        }
    }

    void gets(int... keys) {
        for (int key : keys) {
            int realValue = lruCache.get(key);
            int expectedValue = lruCacheByLinkedHashMap.get(key);
            Assertions.assertEquals(expectedValue, realValue);
        }
    }

    private Set<Integer> set(int... keys) {
        Set<Integer> set = new HashSet<>();
        for (int k : keys) {
            set.add(k);
        }
        return set;
    }
}