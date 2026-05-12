suite("repro_ct_cross_021") {
    sql "DROP TABLE IF EXISTS t_ct_cross_021"
    try {
        sql """
            CREATE TABLE t_ct_cross_021 (id INT, arr ARRAY<INT>, m MAP<STRING,INT>, s STRUCT<a:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_021 SELECT 1, array(1,2), map('a',1), named_struct('a',42)"
        def r = sql "SELECT arr, m, s FROM t_ct_cross_021 WHERE id=1"
        assertTrue(r[0][0] != null && r[0][1] != null && r[0][2] != null, "CT-CROSS-021: select complex client display; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_021" } catch (Exception ignore) {}
    }
}
