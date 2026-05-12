// CT-ARRAY-007: ARRAY of STRUCT<a:INT,b:STRING>
suite("repro_ct_array_007") {
    sql "DROP TABLE IF EXISTS t_ct_array_007"
    try {
        sql """
            CREATE TABLE t_ct_array_007 (id INT, a ARRAY<STRUCT<a:INT,b:STRING>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        // CASE_FLAW fix: parser doesn't accept [named_struct(...)] literal; use array() function via SELECT
        sql "INSERT INTO t_ct_array_007 SELECT 1, array(named_struct('a',1,'b','x'), named_struct('a',2,'b','y'))"
        def r = sql "SELECT array_size(a) FROM t_ct_array_007 WHERE id=1"
        assertEquals(2L, (r[0][0] as Number).longValue(), "CT-ARRAY-007: ARRAY<STRUCT> size=2; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_007" } catch (Exception ignore) {}
    }
}
