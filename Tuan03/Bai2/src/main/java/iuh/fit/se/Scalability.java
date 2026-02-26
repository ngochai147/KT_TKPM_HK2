package iuh.fit.se;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Scalability - Xử lý song song nhiều request bằng thread pool
 */
public class Scalability {
    private final ExecutorService pool;

    public Scalability(int workers) {
        this.pool = Executors.newFixedThreadPool(workers);
    }

    public List<String> processBatch(List<String> requests) throws Exception {
        List<Future<String>> futures = new ArrayList<>();
        for (String req : requests) {
            futures.add(pool.submit(() -> "Done: " + req));
        }

        List<String> results = new ArrayList<>();
        for (Future<String> f : futures) {
            results.add(f.get());
        }
        return results;
    }

    public void shutdown() {
        pool.shutdown();
    }
}

