suite("repro_ct_compat_007") {
    sql "DROP TABLE IF EXISTS t_ct_compat_007"
    try {
        sql """
            CREATE TABLE t_ct_compat_007 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "EXPLAIN SELECT array_size(arr) FROM t_ct_compat_007"
        assertTrue(r.size() > 0, "CT-COMPAT-007: EXPLAIN works; observed lines=${r.size()}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_compat_007" } catch (Exception ignore) {}
    }
}
