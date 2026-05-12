suite("repro_ct_cmp_015") {
    sql "DROP TABLE IF EXISTS t_ct_cmp_015"
    try {
        sql """
            CREATE TABLE t_ct_cmp_015 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cmp_015 VALUES (1, []), (2, [NULL])"
        def r = sql "SELECT arr, count(*) FROM t_ct_cmp_015 GROUP BY arr"
        // hash should distinguish [] vs [NULL]
        assertEquals(2, r.size(), "CT-CMP-015: [] vs [NULL] hash distinct; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cmp_015" } catch (Exception ignore) {}
    }
}
