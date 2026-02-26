package iuh.fit.se;

import java.util.Arrays;
import java.util.List;

public class SystemDemo {
    public static void main(String[] args) throws Exception {

        // 1. AVAILABILITY
        System.out.println("=== 1. AVAILABILITY ===");
        Availability service = new Availability();
        System.out.println("Service UP: " + service.handleRequest("ping"));
        service.setServiceUp(false);
        try { service.handleRequest("ping"); }
        catch (RuntimeException e) { System.out.println("Service DOWN: " + e.getMessage()); }
        service.setServiceUp(true);
        System.out.println("Recovered: " + service.handleRequest("ping"));

        // 2. FAULT TOLERANCE
        System.out.println("\n=== 2. FAULT TOLERANCE ===");
        FaultTolerance ft = new FaultTolerance(3, "fallback-data");
        String result = ft.execute(() -> { throw new RuntimeException("fail"); });
        System.out.println("All retries failed -> fallback: " + result);
        System.out.println("Success case: " + ft.execute(() -> "real-data"));

        // 3. PERFORMANCE CACHE
        System.out.println("\n=== 3. PERFORMANCE CACHE ===");
        PerformanceCache<String, String> cache = new PerformanceCache<>(3);
        cache.put("user:1", "Alice");
        cache.put("user:2", "Bob");
        System.out.println("From cache: " + cache.get("user:1"));
        System.out.println("Cache miss: " + cache.get("user:99"));

        // 4. SCALABILITY
        System.out.println("\n=== 4. SCALABILITY ===");
        Scalability scal = new Scalability(4);
        List<String> results = scal.processBatch(Arrays.asList("r1", "r2", "r3", "r4"));
        results.forEach(System.out::println);
        scal.shutdown();
    }
}

