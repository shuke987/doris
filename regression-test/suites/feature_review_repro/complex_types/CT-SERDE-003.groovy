suite("repro_ct_serde_003") {
    sql "DROP TABLE IF EXISTS t_ct_serde_003"
    try {
        sql """
            CREATE TABLE t_ct_serde_003 (id INT, s STRUCT<a:INT,b:STRING>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_serde_003 SELECT 1, named_struct('a',42,'b','hello')"
        def r = sql "SELECT struct_element(s, 'a'), struct_element(s, 'b') FROM t_ct_serde_003"
        assertEquals(42, (r[0][0] as Number).intValue(), "CT-SERDE-003a; observed=${r}")
        assertEquals("hello", r[0][1].toString(), "CT-SERDE-003b; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_serde_003" } catch (Exception ignore) {}
    }
}
