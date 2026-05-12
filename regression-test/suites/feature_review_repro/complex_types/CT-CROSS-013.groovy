suite("repro_ct_cross_013") {
    sql "DROP TABLE IF EXISTS t_ct_cross_013_a"
    sql "DROP TABLE IF EXISTS t_ct_cross_013_b"
    try {
        sql """
            CREATE TABLE t_ct_cross_013_a (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """
            CREATE TABLE t_ct_cross_013_b (id INT, arr ARRAY<BIGINT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_013_a VALUES (1, [1,2,3])"
        sql "INSERT INTO t_ct_cross_013_b SELECT id, CAST(arr AS ARRAY<BIGINT>) FROM t_ct_cross_013_a"
        def r = sql "SELECT array_size(arr) FROM t_ct_cross_013_b WHERE id=1"
        assertEquals(3L, (r[0][0] as Number).longValue(), "CT-CROSS-013: INSERT SELECT cast; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_013_a" } catch (Exception ignore) {}
        try { sql "DROP TABLE IF EXISTS t_ct_cross_013_b" } catch (Exception ignore) {}
    }
}
