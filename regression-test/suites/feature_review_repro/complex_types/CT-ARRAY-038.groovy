// CT-ARRAY-038: 空数组 [] 写入
suite("repro_ct_array_038") {
    sql "DROP TABLE IF EXISTS t_ct_array_038"
    try {
        sql """
            CREATE TABLE t_ct_array_038 (id INT, a ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_array_038 VALUES (1, [])"
        def r = sql "SELECT array_size(a) FROM t_ct_array_038 WHERE id=1"
        assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-038: empty array size=0; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_038" } catch (Exception ignore) {}
    }
}
