package iuh.fit.se;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScalabilityTest {

    @Test
    void testBatchProcessing() throws Exception {
        Scalability scalability = new Scalability(4);

        List<String> requests = Arrays.asList("req1", "req2", "req3", "req4", "req5");
        List<String> results = scalability.processBatch(requests);

        // Tất cả 5 requests đều được xử lý xong
        assertEquals(5, results.size());
        assertEquals("Done: req1", results.get(0));
        assertEquals("Done: req5", results.get(4));

        scalability.shutdown();
    }
}

