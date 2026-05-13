// IIA-PRS-017: 同列两个不同 parser index（multi-analyzer 支持）
suite("repro_iia_prs_017") {
    sql "DROP TABLE IF EXISTS t_iia_prs_017"
    try {
        sql """
            CREATE TABLE t_iia_prs_017 (id INT, c TEXT,
              INDEX c_idx_en (c) USING INVERTED PROPERTIES('parser'='english'),
              INDEX c_idx_cn (c) USING INVERTED PROPERTIES('parser'='chinese'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        def r = sql "SHOW CREATE TABLE t_iia_prs_017"
        String ddl = r[0][1].toString()
        assertTrue(ddl.contains("c_idx_en") && ddl.contains("c_idx_cn"),
                   "should allow multiple inverted indexes with different parsers on same column; DDL=${ddl.take(200)}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_prs_017" } catch (Exception ignore) {}
    }
}
