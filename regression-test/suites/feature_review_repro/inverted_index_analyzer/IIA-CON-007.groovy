// IIA-CON-007: MATCH_ANY (OR 语义)
suite("repro_iia_con_007") {
    sql "DROP TABLE IF EXISTS t_iia_con_007"
    try {
        sql """
            CREATE TABLE t_iia_con_007 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_con_007 VALUES (1,'apple banana'),(2,'cherry date'),(3,'elderberry')"
        // MATCH_ANY 'apple cherry' → id=1 (apple) OR id=2 (cherry)
        assertEquals(2L, sql("SELECT count(*) FROM t_iia_con_007 WHERE c MATCH_ANY 'apple cherry'")[0][0],
                     "MATCH_ANY 'apple cherry' should hit id=1,2")
        // MATCH_ANY 含 unmatched
        assertEquals(2L, sql("SELECT count(*) FROM t_iia_con_007 WHERE c MATCH_ANY 'apple cherry zzz'")[0][0],
                     "MATCH_ANY with unmatched word still works as OR")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_con_007" } catch (Exception ignore) {}
    }
}
