// IIA-PRS-020: parser=english 索引 'ABC' + MATCH 'abc' 命中 (e2e lowercase)
suite("repro_iia_prs_020") {
    sql "DROP TABLE IF EXISTS t_iia_prs_020"
    try {
        sql """
            CREATE TABLE t_iia_prs_020 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_prs_020 VALUES (1,'ABC'),(2,'XYZ')"
        def r = sql "SELECT count(*) FROM t_iia_prs_020 WHERE c MATCH 'abc'"
        assertEquals(1L, r[0][0],
                     "parser=english default lowercase: MATCH 'abc' should hit 'ABC'")
        def r2 = sql "SELECT count(*) FROM t_iia_prs_020 WHERE c MATCH 'xyz'"
        assertEquals(1L, r2[0][0],
                     "parser=english default lowercase: MATCH 'xyz' should hit 'XYZ'")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_prs_020" } catch (Exception ignore) {}
    }
}
