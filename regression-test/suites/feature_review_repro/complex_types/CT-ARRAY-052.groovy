// CT-ARRAY-052: RENAME COLUMN ARRAY 列
suite("repro_ct_array_052") {
    sql "DROP TABLE IF EXISTS t_ct_array_052"
    try {
        sql """
            CREATE TABLE t_ct_array_052 (id INT, a ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "light_schema_change"="true")
        """
        sql "INSERT INTO t_ct_array_052 VALUES (1, [10,20])"
        sql "ALTER TABLE t_ct_array_052 RENAME COLUMN a b"
        def r = sql "SELECT b FROM t_ct_array_052 WHERE id=1"
        assertEquals(1, r.size(), "CT-ARRAY-052: rename ARRAY col reference updated; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_052" } catch (Exception ignore) {}
    }
}
