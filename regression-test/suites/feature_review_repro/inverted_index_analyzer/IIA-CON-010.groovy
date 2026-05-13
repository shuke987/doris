// IIA-CON-010: MATCH_PHRASE_PREFIX
suite("repro_iia_con_010") {
    sql "DROP TABLE IF EXISTS t_iia_con_010"
    try {
        sql """
            CREATE TABLE t_iia_con_010 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english','support_phrase'='true'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_con_010 VALUES (1,'apache doris fast'),(2,'apache spark'),(3,'cherry')"
        // MATCH_PHRASE_PREFIX 'apache do' → 命中 id=1
        def r = sql "SELECT count(*) FROM t_iia_con_010 WHERE c MATCH_PHRASE_PREFIX 'apache do'"
        assertEquals(1L, r[0][0], "MATCH_PHRASE_PREFIX 'apache do' should hit id=1 (apache + doris matches do prefix)")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_con_010" } catch (Exception ignore) {}
    }
}
