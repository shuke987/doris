// IIA-CRX-002: variant column + parser，必须查 subcolumn (root column 不支持 MATCH)
// 实测发现：VARIANT root column does not support MATCH predicates. Please query a subcolumn instead.
suite("repro_iia_crx_002") {
    sql "DROP TABLE IF EXISTS t_iia_crx_002"
    try {
        sql """
            CREATE TABLE t_iia_crx_002 (id INT, c VARIANT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql """INSERT INTO t_iia_crx_002 VALUES (1, '{"title":"hello world", "tags":["doris","fast"]}')"""
        // variant root MATCH 应被 FE/BE 拒绝
        boolean threw = false
        try {
            sql "SELECT count(*) FROM t_iia_crx_002 WHERE c MATCH 'hello'"
        } catch (Exception e) {
            threw = true
            assertTrue(e.getMessage().toLowerCase().contains("variant") && e.getMessage().toLowerCase().contains("subcolumn"),
                       "Error should mention variant subcolumn requirement; got=${e.getMessage()}")
        }
        assertTrue(threw, "variant root MATCH should be rejected, requires subcolumn access")

        // subcolumn 查询 — c['title'] MATCH 'hello'
        def r = sql "SELECT count(*) FROM t_iia_crx_002 WHERE c['title'] MATCH 'hello'"
        assertNotNull(r, "subcolumn MATCH should not crash; r=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_crx_002" } catch (Exception ignore) {}
    }
}
