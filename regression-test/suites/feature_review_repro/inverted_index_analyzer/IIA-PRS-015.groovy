// IIA-PRS-015: parser=none 整字段比对（MATCH 必须用完全相同字符串）
suite("repro_iia_prs_015") {
    sql "DROP TABLE IF EXISTS t_iia_prs_015"
    try {
        sql """
            CREATE TABLE t_iia_prs_015 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='none'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_prs_015 VALUES (1,'hello world'),(2,'world')"
        // MATCH 子串不命中
        def r1 = sql "SELECT count(*) FROM t_iia_prs_015 WHERE c MATCH 'hello'"
        assertEquals(0L, r1[0][0], "parser=none: MATCH 'hello' should NOT hit 'hello world' (no tokenization)")
        // MATCH 完整字段命中
        def r2 = sql "SELECT count(*) FROM t_iia_prs_015 WHERE c MATCH 'hello world'"
        assertEquals(1L, r2[0][0], "parser=none: MATCH 'hello world' should hit id=1")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_prs_015" } catch (Exception ignore) {}
    }
}
