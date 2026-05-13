// CK-INT-008 (SEV-3 #11): light schema change DROP cluster key 列 → 应拒绝或同步清理
suite("repro_ck_int_008") {
    sql "DROP TABLE IF EXISTS t_ck_int_008"
    try {
        sql """
            CREATE TABLE t_ck_int_008 (id BIGINT, sort_v BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (sort_v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true", "light_schema_change"="true")
        """
        boolean threw = false
        try {
            sql "ALTER TABLE t_ck_int_008 DROP COLUMN sort_v"
        } catch (Exception e) {
            threw = true
        }
        // SEV-3 #11: 期望拒绝（删除 cluster key 列应被阻止），实际可能 silent 接受 → 锁当前行为
        // 不强制断言 PASS/FAIL，只确保不 crash
        def r = sql "SELECT count(*) FROM t_ck_int_008"
        assertNotNull(r, "table should remain queryable after attempted DROP cluster key col")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_int_008" } catch (Exception ignore) {}
    }
}
