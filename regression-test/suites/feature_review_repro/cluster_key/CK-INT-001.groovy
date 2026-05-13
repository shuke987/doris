// CK-INT-001: ALTER TABLE 不能修改 cluster key
suite("repro_ck_int_001") {
    sql "DROP TABLE IF EXISTS t_ck_int_001"
    try {
        sql """
            CREATE TABLE t_ck_int_001 (id BIGINT, v BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        boolean threw = false
        try {
            sql "ALTER TABLE t_ck_int_001 MODIFY ORDER BY (v)"
        } catch (Exception e) {
            threw = true
        }
        assertTrue(threw, "ALTER TABLE MODIFY ORDER BY should not be supported")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_int_001" } catch (Exception ignore) {}
    }
}
