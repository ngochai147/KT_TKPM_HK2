package com.example.demo.service;

import com.example.demo.config.PartitioningStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service quản lý Horizontal Partitioning
 * Xác định user thuộc partition nào
 */
@Slf4j
@Service
public class PartitioningService {

    /**
     * Lấy partition index của user
     * @param userId ID của user
     * @return partition index (0-9)
     */
    public int getPartitionIndex(Long userId) {
        return PartitioningStrategy.getPartitionIndex(userId);
    }

    /**
     * Lấy database name của user
     * @param userId ID của user
     * @return database name (user_db_0 ... user_db_9)
     */
    public String getDatabaseName(Long userId) {
        return PartitioningStrategy.getDatabaseName(userId);
    }

    /**
     * Log thông tin partitioning
     */
    public void logPartitionInfo(Long userId) {
        int partition = getPartitionIndex(userId);
        String dbName = getDatabaseName(userId);
        log.info("User {} -> Partition {} -> Database: {}", userId, partition, dbName);
    }

    /**
     * Kiểm tra user có thuộc partition này không
     */
    public boolean isUserInPartition(Long userId, int partitionIndex) {
        return getPartitionIndex(userId) == partitionIndex;
    }

    /**
     * Lấy tất cả database names
     */
    public String[] getAllDatabaseNames() {
        return PartitioningStrategy.getAllDatabaseNames();
    }

    /**
     * Lấy tổng số partitions
     */
    public int getTotalPartitions() {
        return PartitioningStrategy.getPartitionCount();
    }
}
