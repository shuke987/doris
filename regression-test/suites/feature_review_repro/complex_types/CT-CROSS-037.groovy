suite("repro_ct_cross_037") {
    sql "DROP TABLE IF EXISTS t_ct_cross_037"
    try {
        sql """
            CREATE TABLE t_ct_cross_037 (id INT, arr ARRAY<INT> REPLACE_IF_NOT_NULL)
            AGGREGATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_037 VALUES (1, [1,2]), (1, NULL), (1, [3,4])"
        def r = sql "SELECT arr FROM t_ct_cross_037 WHERE id=1"
        assertTrue(r[0][0] != null, "CT-CROSS-037: REPLACE_IF_NOT_NULL keeps non-NULL; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_037" } catch (Exception ignore) {}
    }
}
