// CT-ARRAY-005: ARRAY of ARRAY<INT>（嵌套）
suite("repro_ct_array_005") {
    sql "DROP TABLE IF EXISTS t_ct_array_005"
    try {
        sql """
            CREATE TABLE t_ct_array_005 (id INT, a ARRAY<ARRAY<INT>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_array_005 VALUES (1, [[1,2],[3,4]])"
        def r = sql "SELECT array_size(a) FROM t_ct_array_005 WHERE id=1"
        assertEquals(2L, (r[0][0] as Number).longValue(), "CT-ARRAY-005: nested ARRAY size=2; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_005" } catch (Exception ignore) {}
    }
}
