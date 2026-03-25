package com.example.demo.controller;

import com.example.demo.service.PartitioningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller cho Horizontal Partitioning Information
 * Giúp hiểu cách dữ liệu được phân chia
 */
@Slf4j
@RestController
@RequestMapping("/api/partitioning")
@RequiredArgsConstructor
public class PartitioningInfoController {

    private final PartitioningService partitioningService;

    /**
     * GET /api/partitioning/info/{userId}
     * Lấy thông tin partition của user
     *
     * Ví dụ:
     * - userId=1 -> partition 1, database: user_db_1
     * - userId=10 -> partition 0, database: user_db_0
     * - userId=123 -> partition 3, database: user_db_3
     */
    @GetMapping("/info/{userId}")
    public ResponseEntity<?> getPartitionInfo(@PathVariable Long userId) {
        int partitionIndex = partitioningService.getPartitionIndex(userId);
        String databaseName = partitioningService.getDatabaseName(userId);

        partitioningService.logPartitionInfo(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("userId", userId);
        response.put("partitionIndex", partitionIndex);
        response.put("databaseName", databaseName);
        response.put("message", String.format("User %d belongs to %s", userId, databaseName));

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/partitioning/all-databases
     * Liệt kê tất cả databases trong hệ thống
     */
    @GetMapping("/all-databases")
    public ResponseEntity<?> getAllDatabases() {
        String[] databases = partitioningService.getAllDatabaseNames();
        int total = partitioningService.getTotalPartitions();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("totalPartitions", total);
        response.put("databases", databases);
        response.put("message", String.format("System has %d partitions for horizontal partitioning", total));

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/partitioning/test
     * Test endpoint với 100 example users
     * Hiển thị cách 100 users được phân chia vào 10 databases
     */
    @GetMapping("/test")
    public ResponseEntity<?> testPartitioning() {
        Map<Integer, Integer> partitionDistribution = new HashMap<>();

        // Khởi tạo distribution
        for (int i = 0; i < partitioningService.getTotalPartitions(); i++) {
            partitionDistribution.put(i, 0);
        }

        // Phân phối 100 users
        for (long userId = 1; userId <= 100; userId++) {
            int partition = partitioningService.getPartitionIndex(userId);
            partitionDistribution.put(partition, partitionDistribution.get(partition) + 1);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("testUsersCount", 100);
        response.put("totalPartitions", partitioningService.getTotalPartitions());
        response.put("distribution", partitionDistribution);
        response.put("message", "Distribution of 100 users across 10 partitions");

        // Ví dụ chi tiết cho 10 users đầu tiên
        Map<Long, String> examples = new HashMap<>();
        for (long userId = 1; userId <= 10; userId++) {
            examples.put(userId, partitioningService.getDatabaseName(userId));
        }
        response.put("examplesForFirst10Users", examples);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/partitioning/large-scale-test
     * Test với 10,000 users để hiển thị horizontal partitioning
     */
    @GetMapping("/large-scale-test")
    public ResponseEntity<?> largeScaleTest() {
        Map<Integer, Integer> partitionDistribution = new HashMap<>();

        // Khởi tạo distribution
        for (int i = 0; i < partitioningService.getTotalPartitions(); i++) {
            partitionDistribution.put(i, 0);
        }

        // Phân phối 10,000 users
        for (long userId = 1; userId <= 10000; userId++) {
            int partition = partitioningService.getPartitionIndex(userId);
            partitionDistribution.put(partition, partitionDistribution.get(partition) + 1);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("scenario", "10,000 users distributed across 10 databases");
        response.put("testUsersCount", 10000);
        response.put("totalPartitions", partitioningService.getTotalPartitions());
        response.put("distribution", partitionDistribution);
        response.put("averageUsersPerDatabase", 10000 / partitioningService.getTotalPartitions());
        response.put("message", "With Horizontal Partitioning, 10,000 users are evenly distributed");

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/partitioning/formula
     * Giải thích công thức partitioning
     */
    @GetMapping("/formula")
    public ResponseEntity<?> getFormula() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("partitioningFormula", "partition_index = user_id % PARTITION_COUNT");
        response.put("partitionCount", partitioningService.getTotalPartitions());
        response.put("examples", Map.of(
            "user_id=1", "1 % 10 = 1 -> user_db_1",
            "user_id=10", "10 % 10 = 0 -> user_db_0",
            "user_id=15", "15 % 10 = 5 -> user_db_5",
            "user_id=123", "123 % 10 = 3 -> user_db_3",
            "user_id=5000", "5000 % 10 = 0 -> user_db_0",
            "user_id=9999", "9999 % 10 = 9 -> user_db_9"
        ));
        response.put("explanation", "Mỗi user được gán vào một partition dựa trên user_id. " +
            "Điều này giúp phân tán dữ liệu đều đặn và cải thiện hiệu suất");

        return ResponseEntity.ok(response);
    }
}
