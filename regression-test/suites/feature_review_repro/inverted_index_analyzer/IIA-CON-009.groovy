// IIA-CON-009: MATCH_REGEXP
suite("repro_iia_con_009") {
    sql "DROP TABLE IF EXISTS t_iia_con_009"
    try {
        sql """
            CREATE TABLE t_iia_con_009 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_con_009 VALUES (1,'doris123'),(2,'foobar'),(3,'doris2026')"
        // doris\\d+ regex
        def r = sql "SELECT count(*) FROM t_iia_con_009 WHERE c MATCH_REGEXP 'doris[0-9]+'"
        assertEquals(2L, r[0][0], "MATCH_REGEXP 'doris[0-9]+' should hit id=1,3")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_con_009" } catch (Exception ignore) {}
    }
}
