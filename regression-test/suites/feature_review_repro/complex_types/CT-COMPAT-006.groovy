suite("repro_ct_compat_006") {
    sql "DROP TABLE IF EXISTS t_ct_compat_006"
    try {
        sql """
            CREATE TABLE t_ct_compat_006 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_compat_006 VALUES (1, [1,2,3])"
        def r = sql "SELECT id, array_size(arr) FROM t_ct_compat_006"
        assertEquals(3L, (r[0][1] as Number).longValue(), "CT-COMPAT-006: nereids plan with complex; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_compat_006" } catch (Exception ignore) {}
    }
}
