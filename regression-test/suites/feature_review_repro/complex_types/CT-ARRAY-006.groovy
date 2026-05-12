// CT-ARRAY-006: ARRAY of MAP<STRING,INT>
suite("repro_ct_array_006") {
    sql "DROP TABLE IF EXISTS t_ct_array_006"
    try {
        sql """
            CREATE TABLE t_ct_array_006 (id INT, a ARRAY<MAP<STRING,INT>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        // CASE_FLAW fix: parser doesn't accept [map(...)] literal; use array() function via SELECT
        sql "INSERT INTO t_ct_array_006 SELECT 1, array(map('a',1), map('b',2))"
        def r = sql "SELECT array_size(a) FROM t_ct_array_006 WHERE id=1"
        assertEquals(2L, (r[0][0] as Number).longValue(), "CT-ARRAY-006: ARRAY<MAP> size=2; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_006" } catch (Exception ignore) {}
    }
}
