suite("repro_ct_struct_029") {
    sql "DROP TABLE IF EXISTS t_ct_struct_029"
    try {
        sql """
            CREATE TABLE t_ct_struct_029 (id INT, s STRUCT<a:INT, b:STRING>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_struct_029 SELECT 1, struct(CAST(NULL AS INT), 'a')"
        def r = sql "SELECT s FROM t_ct_struct_029 WHERE id=1"
        assertEquals(1, r.size(), "CT-STRUCT-029: NULL field works; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_029" } catch (Exception ignore) {}
    }
}
