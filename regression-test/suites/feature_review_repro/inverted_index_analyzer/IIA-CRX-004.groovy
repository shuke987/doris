// IIA-CRX-004: array<text> + parser=english → FE REJECT
// 实测：FE legacy InvertedIndexUtil.checkColumn (line 215-218) 拒绝 ANY non-none parser on array column
// 错误消息: "INVERTED index with parser: english is not supported for array column"
// Doc gap: overview.md 未明确该限制，但 schema_change/array 支持限定。
suite("repro_iia_crx_004") {
    sql "DROP TABLE IF EXISTS t_iia_crx_004"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_crx_004 (id INT, tags ARRAY<TEXT>,
              INDEX tags_idx (tags) USING INVERTED PROPERTIES('parser'='english'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
        assertTrue(e.getMessage().toLowerCase().contains('array'),
                   "Error msg should mention array; got=${e.getMessage()}")
        assertTrue(e.getMessage().contains('parser'),
                   "Error msg should mention parser; got=${e.getMessage()}")
    }
    assertTrue(threw, "FE should reject parser on array column (legacy checkColumn rejects any non-none parser)")

    // 但 ARRAY<TEXT> 不指定 parser 应该是合法的（整字段索引）
    sql "DROP TABLE IF EXISTS t_iia_crx_004b"
    try {
        sql """
            CREATE TABLE t_iia_crx_004b (id INT, tags ARRAY<TEXT>,
              INDEX tags_idx (tags) USING INVERTED)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        def r = sql "SHOW CREATE TABLE t_iia_crx_004b"
        assertNotNull(r, "ARRAY<TEXT> + INVERTED without parser should be accepted")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_crx_004b" } catch (Exception ignore) {}
    }
}
