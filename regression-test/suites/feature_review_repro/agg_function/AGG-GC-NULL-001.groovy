// AGG-GC-NULL-001 (Second-pass review · R2: GROUP_CONCAT(s, NULL) returns NULL)
// Spec: MySQL/Snowflake — GROUP_CONCAT 的 separator 为 NULL 时退化为默认 ','，结果非 NULL。
// 当前 4.1: 整结果返 NULL → 与 MySQL 不一致 → FAIL (锁定与 MySQL 一致的期望)
// 参考：MySQL docs https://dev.mysql.com/doc/refman/8.0/en/aggregate-functions.html#function_group-concat
suite("repro_agg_gc_null_001") {
    sql "DROP TABLE IF EXISTS t_agg_gc_null"
    try {
        sql """
            CREATE TABLE t_agg_gc_null (id INT, s VARCHAR(20))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_agg_gc_null VALUES (1, 'a'), (2, 'b'), (3, 'c')"
        def r = sql "SELECT GROUP_CONCAT(s, NULL) FROM t_agg_gc_null"
        // 期望：与 MySQL 一致，NULL separator 退化为 ','；至少不应整结果 NULL
        assertNotNull(r[0][0],
            "GROUP_CONCAT(s, NULL) must NOT return NULL — MySQL falls back to default separator ','")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_gc_null" } catch (Exception ignore) {}
    }
}
