// IIA-CON-008: MATCH_ALL (AND 语义)
suite("repro_iia_con_008") {
    sql "DROP TABLE IF EXISTS t_iia_con_008"
    try {
        sql """
            CREATE TABLE t_iia_con_008 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_con_008 VALUES (1,'apple banana'),(2,'apple cherry'),(3,'banana cherry')"
        // MATCH_ALL 'apple banana' → 只 id=1 (含两 token)
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_con_008 WHERE c MATCH_ALL 'apple banana'")[0][0],
                     "MATCH_ALL 'apple banana' should only hit id=1")
        // MATCH_ALL 含 unmatched → 0
        assertEquals(0L, sql("SELECT count(*) FROM t_iia_con_008 WHERE c MATCH_ALL 'apple banana zzz'")[0][0],
                     "MATCH_ALL with unmatched word should give 0")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_con_008" } catch (Exception ignore) {}
    }
}
