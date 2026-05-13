// IIA-CON-001: 索引/查询同 analyzer (parser=english lowercase 双向)
suite("repro_iia_con_001") {
    sql "DROP TABLE IF EXISTS t_iia_con_001"
    try {
        sql """
            CREATE TABLE t_iia_con_001 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_con_001 VALUES (1,'Hello WORLD'),(2,'foo bar')"
        // 索引时 lowercase, 查询时 lowercase → MATCH 'word' / 'WORLD' 都命中
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_con_001 WHERE c MATCH 'world'")[0][0],
                     "MATCH 'world' should hit lowercased 'WORLD'")
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_con_001 WHERE c MATCH 'WORLD'")[0][0],
                     "MATCH 'WORLD' should hit (query also lowercased)")
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_con_001 WHERE c MATCH 'hello'")[0][0],
                     "MATCH 'hello' should hit")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_con_001" } catch (Exception ignore) {}
    }
}
