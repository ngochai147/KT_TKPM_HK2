package iuh.fit.se;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PerformanceCacheTest {

    @Test
    void testCachePutGetAndLRU() {
        PerformanceCache<String, String> cache = new PerformanceCache<>(3);

        // Put & Get
        cache.put("a", "1");
        cache.put("b", "2");
        cache.put("c", "3");
        assertEquals("1", cache.get("a"));
        assertEquals(3, cache.size());

        // LRU: thêm phần tử thứ 4 -> phần tử ít dùng nhất bị xóa
        // "a" vừa được get nên "b" là ít dùng nhất
        cache.put("d", "4");
        assertNull(cache.get("b")); // b bị loại
        assertEquals("4", cache.get("d"));
    }
}

