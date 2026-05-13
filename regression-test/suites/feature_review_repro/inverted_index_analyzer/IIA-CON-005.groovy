// IIA-CON-005: MATCH_PHRASE 词序敏感
suite("repro_iia_con_005") {
    sql "DROP TABLE IF EXISTS t_iia_con_005"
    try {
        sql """
            CREATE TABLE t_iia_con_005 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english', 'support_phrase'='true'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        // id=1: 'doris is fast' → phrase 'doris is' 命中
        // id=2: 'is doris fast' → 'is' 在 'doris' 之前，phrase 'doris is' 不命中
        sql "INSERT INTO t_iia_con_005 VALUES (1,'doris is fast'),(2,'is doris fast')"
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_con_005 WHERE c MATCH_PHRASE 'doris is'")[0][0],
                     "MATCH_PHRASE 'doris is' should only hit id=1 (in order)")
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_con_005 WHERE c MATCH_PHRASE 'is doris'")[0][0],
                     "MATCH_PHRASE 'is doris' should only hit id=2 (reversed order)")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_con_005" } catch (Exception ignore) {}
    }
}
