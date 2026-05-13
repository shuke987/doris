// IIA-PRS-018-en: array<text> + 无 parser (因 array+parser FE 拒绝)
// 验证 array 元素整体作为 token 索引
suite("repro_iia_prs_018_en") {
    sql "DROP TABLE IF EXISTS t_iia_prs_018_en"
    try {
        sql """
            CREATE TABLE t_iia_prs_018_en (id INT, tags ARRAY<TEXT>,
              INDEX tags_idx (tags) USING INVERTED)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_prs_018_en VALUES (1, ['hello','world']),(2, ['doris','foo'])"
        // 元素整体 token：MATCH 'hello' 命中
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_prs_018_en WHERE tags MATCH 'hello'")[0][0],
                     "array no-parser: MATCH 'hello' should hit array containing 'hello'")
        // MATCH 子串不命中
        assertEquals(0L, sql("SELECT count(*) FROM t_iia_prs_018_en WHERE tags MATCH 'hell'")[0][0],
                     "array no-parser: MATCH partial 'hell' should NOT hit")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_prs_018_en" } catch (Exception ignore) {}
    }
}
