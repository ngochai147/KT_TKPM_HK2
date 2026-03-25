package com.example.demo.config;

/**
 * Chiến lược partitioning cho Horizontal Partitioning
 * Chia users vào multiple databases dựa trên user_id
 */
public class PartitioningStrategy {
    // Số lượng partitions (databases)
    private static final int PARTITION_COUNT = 10;

    /**
     * Xác định partition index dựa trên user_id
     * Formula: user_id % PARTITION_COUNT
     *
     * Ví dụ với 10 partitions:
     * - user_id = 1 -> partition 1
     * - user_id = 10 -> partition 0
     * - user_id = 15 -> partition 5
     * - user_id = 123 -> partition 3
     */
    public static int getPartitionIndex(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid userId: " + userId);
        }
        return (int) (userId % PARTITION_COUNT);
    }

    /**
     * Lấy database name dựa trên user_id
     * Ví dụ: user_db_0, user_db_1, ..., user_db_9
     */
    public static String getDatabaseName(Long userId) {
        int partitionIndex = getPartitionIndex(userId);
        return "user_db_" + partitionIndex;
    }

    /**
     * Lấy tổng số partitions
     */
    public static int getPartitionCount() {
        return PARTITION_COUNT;
    }

    /**
     * Lấy tất cả database names
     */
    public static String[] getAllDatabaseNames() {
        String[] databases = new String[PARTITION_COUNT];
        for (int i = 0; i < PARTITION_COUNT; i++) {
            databases[i] = "user_db_" + i;
        }
        return databases;
    }
}
