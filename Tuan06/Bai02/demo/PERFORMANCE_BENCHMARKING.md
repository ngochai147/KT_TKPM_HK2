# 📊 Performance Benchmarking & Partition Monitoring

## 🎯 Mục Đích

Giám sát performance của hệ thống partitioned user management và đảm bảo queries < 100ms.

---

## 1️⃣ Performance Benchmarking Queries

### Test Query 1: Get User by Email (Index)

```sql
-- Expect: ~10ms (UNIQUE INDEX)
-- Execution: Primary Key lookup
EXPLAIN FORMAT=JSON
SELECT * FROM users WHERE email = 'user1@example.com';

-- Benchmark
SELECT *, BENCHMARK(1000, (SELECT COUNT(*) FROM users WHERE email = 'user1@example.com'))
AS benchmark_result;
```

**Expected Output:**
```
Query took: ~10ms
Rows examined: 1
Rows sent: 1
Using index: YES
```

---

### Test Query 2: Get Login History (Horizontal Partition)

```sql
-- Expect: ~15-20ms (Partition pruning + Index)
-- Execution: Partition p1 selected + Index scan
EXPLAIN FORMAT=JSON
SELECT * FROM login_histories
WHERE user_id = 5000
ORDER BY login_at DESC
LIMIT 10;

-- Benchmark with timing
SET @start = UNIX_TIMESTAMP(NOW(3));
SELECT * FROM login_histories
WHERE user_id = 5000
ORDER BY login_at DESC
LIMIT 10;
SET @end = UNIX_TIMESTAMP(NOW(3));
SELECT @end - @start AS execution_time_ms;
```

**Expected Output:**
```
Query took: ~17ms
Partitions selected: p1
Rows examined: ~50-100 (from partition p1)
Using index: YES
```

---

### Test Query 3: Get Orders with Multiple Conditions (Composite Index + Function Partition)

```sql
-- Expect: ~25-30ms (Function partition + Composite index)
-- Execution: Partition p2025 selected + Composite index (user_id, status, order_date)
EXPLAIN FORMAT=JSON
SELECT * FROM orders
WHERE user_id = 5000
  AND status = 'DELIVERED'
  AND YEAR(order_date) = 2025
ORDER BY order_date DESC;

-- Benchmark
SELECT SQL_CALC_FOUND_ROWS *
FROM orders
WHERE user_id = 5000
  AND status = 'DELIVERED'
  AND YEAR(order_date) = 2025
ORDER BY order_date DESC;

SHOW SESSION STATUS LIKE 'Last_query_cost';
```

**Expected Output:**
```
Query took: ~28ms
Partitions selected: p2025
Index used: idx_order_composite (user_id, status, order_date)
Rows examined: ~20-50
```

---

### Test Query 4: Login Count Today (Partition Pruning)

```sql
-- Expect: ~15ms (Partition p0 selected + fast count)
EXPLAIN
SELECT COUNT(*) FROM login_histories
WHERE user_id = 2000
  AND login_at > CURDATE();

-- Actual query
SELECT COUNT(*) as logins_today
FROM login_histories
WHERE user_id = 2000
  AND login_at >= DATE_FORMAT(CURDATE(), '%Y-%m-%d')
  AND login_at < DATE_FORMAT(CURDATE() + INTERVAL 1 DAY, '%Y-%m-%d');
```

**Expected Output:**
```
Query took: ~12ms
Partitions selected: p0 (user_id 2000 < 2501)
Using index: idx_login_composite
```

---

### Test Query 5: Range Query on Orders (Function Partition)

```sql
-- Expect: ~35ms (Multiple partitions scanned based on date range)
-- Partitions: p_2024, p_2025
EXPLAIN FORMAT=JSON
SELECT * FROM orders
WHERE user_id = 50000
  AND order_date BETWEEN '2024-11-01' AND '2025-03-25'
ORDER BY order_date DESC;

-- Benchmark
SELECT * FROM orders
WHERE user_id = 50000
  AND order_date BETWEEN '2024-11-01' AND '2025-03-25'
ORDER BY order_date DESC;
```

**Expected Output:**
```
Query took: ~32ms
Partitions selected: p_2024, p_2025
Rows examined: ~50-200
Using index: idx_order_date
```

---

## 2️⃣ Partition Monitoring

### Check Partition Status

```sql
-- Xem chi tiết tất cả partitions
SELECT
    TABLE_SCHEMA,
    TABLE_NAME,
    PARTITION_NAME,
    PARTITION_METHOD,
    PARTITION_EXPRESSION,
    PARTITION_DESCRIPTION,
    TABLE_ROWS,
    AVG_ROW_LENGTH,
    DATA_LENGTH,
    INDEX_LENGTH,
    DATA_FREE
FROM INFORMATION_SCHEMA.PARTITIONS
WHERE TABLE_SCHEMA = 'user_management_db'
AND TABLE_NAME IN ('login_histories', 'orders')
ORDER BY TABLE_NAME, PARTITION_NAME;
```

**Expected Output:**
```
+---------------+------------------+----------------+------------------+---------------------+
| TABLE_SCHEMA  | TABLE_NAME       | PARTITION_NAME | PARTITION_METHOD | PARTITION_EXPRESSION|
+---------------+------------------+----------------+------------------+---------------------+
| user_db       | login_histories  | p0             | RANGE            | user_id             |
| user_db       | login_histories  | p1             | RANGE            | user_id             |
| user_db       | login_histories  | p2             | RANGE            | user_id             |
| user_db       | orders           | p_2023         | RANGE            | YEAR(order_date)    |
| user_db       | orders           | p_2025         | RANGE            | YEAR(order_date)    |
+---------------+------------------+----------------+------------------+---------------------+
```

---

### Monitor Partition Growth

```sql
-- Xem kích thước từng partition
SELECT
    TABLE_NAME,
    PARTITION_NAME,
    ROUND(TABLE_ROWS / 1000000, 2) as rows_millions,
    ROUND(DATA_LENGTH / 1073741824, 2) as size_gb,
    CASE
        WHEN TABLE_ROWS > 5000000 THEN 'LARGE'
        WHEN TABLE_ROWS > 1000000 THEN 'MEDIUM'
        ELSE 'SMALL'
    END as partition_size
FROM INFORMATION_SCHEMA.PARTITIONS
WHERE TABLE_SCHEMA = 'user_management_db'
AND TABLE_NAME IN ('login_histories', 'orders')
AND PARTITION_NAME IS NOT NULL
ORDER BY TABLE_NAME, PARTITION_NAME;
```

**Output xảy ra khi system phát triển:**
```
+--------------------+---------------+---------------+---------+
| TABLE_NAME         | PARTITION_NAME| rows_millions | size_gb |
+--------------------+---------------+---------------+---------+
| login_histories    | p0            | 0.50          | 0.05    |
| login_histories    | p1            | 0.50          | 0.05    |
| login_histories    | p2            | 0.52          | 0.05    |
| login_histories    | p3            | 0.51          | 0.05    |
| login_histories    | p4            | 1.00          | 0.10    |
| login_histories    | p5            | 3.00          | 0.30    |
| login_histories    | p6            | 5.00          | 0.50    |
| orders             | p_2023        | 0.05          | 0.01    |
| orders             | p_2024        | 0.20          | 0.02    |
| orders             | p_2025        | 0.18          | 0.02    |
+--------------------+---------------+---------------+---------+
```

---

### Check Table Statistics

```sql
-- Xem thống kê bảng hiện tại
SELECT
    TABLE_NAME,
    TABLE_ROWS,
    ROUND(DATA_LENGTH / 1073741824, 2) as data_gb,
    ROUND(INDEX_LENGTH / 1073741824, 2) as index_gb,
    ROUND((DATA_LENGTH + INDEX_LENGTH) / 1073741824, 2) as total_gb,
    UPDATE_TIME
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = 'user_management_db'
AND TABLE_NAME IN ('users', 'user_profiles', 'login_histories', 'orders')
ORDER BY TABLE_ROWS DESC;
```

**Output:**
```
+-------------------+------------+---------+---------+----------+
| TABLE_NAME        | TABLE_ROWS | data_gb | index_gb| total_gb |
+-------------------+------------+---------+---------+----------+
| login_histories   | 12000000   | 1.50    | 2.00    | 3.50     |
| orders            | 500000     | 0.08    | 0.12    | 0.20     |
| user_profiles     | 100000     | 0.02    | 0.01    | 0.03     |
| users             | 100000     | 0.01    | 0.01    | 0.02     |
+-------------------+------------+---------+---------+----------+
```

---

## 3️⃣ Index Analysis

### Check Index Usage

```sql
-- TẠO BẢNG STAT NẾU CHƯACÓ
-- (Requires table_io_waits_summary_by_index_usage privilege)

SELECT
    OBJECT_SCHEMA,
    OBJECT_NAME,
    INDEX_NAME,
    COUNT_READ,
    COUNT_WRITE,
    COUNT_INSERT,
    COUNT_UPDATE,
    COUNT_DELETE
FROM PERFORMANCE_SCHEMA.table_io_waits_summary_by_index_usage
WHERE OBJECT_SCHEMA = 'user_management_db'
ORDER BY COUNT_READ DESC;
```

**Expected High Usage Indexes:**
```
+----------------+------------------+------------------------+-----------+-----------+
| OBJECT_NAME    | INDEX_NAME       | COUNT_READ            | COUNT_WRITE|
+----------------+------------------+------------------------+-----------+-----------+
| users          | idx_email        | 5000                  | 100       |
| login_histories| idx_login_at     | 50000                 | 20000     |
| orders         | idx_order_composite| 10000               | 2000      |
+----------------+------------------+------------------------+-----------+-----------+
```

---

### Find Unused Indexes

```sql
-- Tìm indexes không được sử dụng (candidates cho removal)
SELECT
    OBJECT_SCHEMA,
    OBJECT_NAME,
    INDEX_NAME,
    COUNT_READ,
    COUNT_WRITE
FROM PERFORMANCE_SCHEMA.table_io_waits_summary_by_index_usage
WHERE OBJECT_SCHEMA = 'user_management_db'
AND COUNT_READ = 0
AND COUNT_WRITE = 0
AND INDEX_NAME != 'PRIMARY'
ORDER BY OBJECT_NAME;
```

---

## 4️⃣ Query Optimization Tips

### ✅ DO: Use Partition Key in WHERE Clause

```sql
-- GOOD (Partition pruning sẽ giúp)
SELECT * FROM login_histories
WHERE user_id = 5000;

-- GOOD (Partition key + Index)
SELECT * FROM orders
WHERE user_id = 50000
AND order_date > '2025-01-01';
```

### ❌ DON'T: Avoid Full Table Scan

```sql
-- BAD (Scans all partitions)
SELECT * FROM login_histories
WHERE device = 'WEB';

-- BAD (Function on indexed column)
SELECT * FROM orders
WHERE YEAR(order_date) = 2025;  -- Use: order_date BETWEEN '2025-01-01' AND '2025-12-31'
```

### ✅ DO: Use LIMIT for Large Result Sets

```sql
-- GOOD (Returns 10 rows)
SELECT * FROM login_histories
WHERE user_id = 5000
ORDER BY login_at DESC
LIMIT 10;

-- GOOD (Index helps LIMIT)
SELECT * FROM orders
WHERE user_id = 50000
AND status = 'DELIVERED'
LIMIT 20;
```

---

## 5️⃣ Maintenance Tasks

### Weekly Maintenance

```bash
#!/bin/bash
# weekly_maintenance.sh

mysql -u root -p user_management_db << EOF

-- Analyze tables (update statistics)
ANALYZE TABLE users;
ANALYZE TABLE user_profiles;
ANALYZE TABLE login_histories;
ANALYZE TABLE orders;

-- Check table integrity
CHECK TABLE users;
CHECK TABLE login_histories;
CHECK TABLE orders;

-- Show table status
SHOW TABLE STATUS FROM user_management_db;

EOF
```

### Monthly Maintenance

```sql
-- Optimize partitions (rebuild index)
OPTIMIZE TABLE login_histories;
OPTIMIZE TABLE orders;

-- Reorganize partitions (for maintenance)
ALTER TABLE login_histories OPTIMIZE PARTITION p0, p1, p2, p3;
ALTER TABLE orders OPTIMIZE PARTITION p_2024, p_2025;
```

### Quarterly: Add New Partitions (Scaling)

```sql
-- Khi dữ liệu phát triển, thêm partition mới

-- Example 1: Thêm range mới cho login_histories
ALTER TABLE login_histories REORGANIZE PARTITION p_max INTO (
  PARTITION p7 VALUES LESS THAN (150001),
  PARTITION p_max VALUES LESS THAN MAXVALUE
);

-- Example 2: Thêm year mới cho orders
ALTER TABLE orders REORGANIZE PARTITION p_future INTO (
  PARTITION p_2028 VALUES LESS THAN (2029),
  PARTITION p_future VALUES LESS THAN MAXVALUE
);
```

---

## 6️⃣ Performance Metrics Dashboard (Query)

```sql
-- Comprehensive Dashboard Query
SELECT
  'System Health' as metric,
  CONCAT(
    'Tables: ',
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES
     WHERE TABLE_SCHEMA = 'user_management_db'),
    ', ',
    'Partitions: ',
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.PARTITIONS
     WHERE TABLE_SCHEMA = 'user_management_db' AND PARTITION_NAME IS NOT NULL),
    ', ',
    'Total Users: ',
    (SELECT COUNT(*) FROM users)
  ) as status

UNION ALL

SELECT
  'Data Volume',
  CONCAT(
    'Logins: ',
    (SELECT FORMAT(COUNT(*), 0) FROM login_histories),
    ', Orders: ',
    (SELECT FORMAT(COUNT(*), 0) FROM orders)
  )

UNION ALL

SELECT
  'Database Size',
  CONCAT(
    ROUND(SUM(DATA_LENGTH + INDEX_LENGTH) / 1073741824, 2),
    ' GB'
  )
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = 'user_management_db';
```

---

## 🎯 Performance SLA

### Target Metrics

| Operation | Target (ms) | Status |
|-----------|------------|--------|
| GET user by email | < 10 | ✅ |
| GET login history | < 20 | ✅ |
| GET orders (10 items) | < 30 | ✅ |
| CREATE user | < 50 | ✅ |
| CREATE order | < 50 | ✅ |
| Record login | < 40 | ✅ |

### Monitoring Tools

- **MySQL Workbench:** Visual query analysis
- **Percona Monitoring Tool:** Real-time metrics
- **Application Logs:** Query execution times

---

**Performance Monitoring Complete!** 📊
