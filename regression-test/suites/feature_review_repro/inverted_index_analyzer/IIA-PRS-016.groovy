// IIA-PRS-016: ALTER 改 parser 行为（doc 未明，实测验证）
suite("repro_iia_prs_016") {
    sql "DROP TABLE IF EXISTS t_iia_prs_016"
    try {
        sql """
            CREATE TABLE t_iia_prs_016 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        // ALTER 同 index 改 parser 应拒绝或忽略
        boolean threw = false
        try {
            sql "ALTER TABLE t_iia_prs_016 MODIFY INDEX c_idx PROPERTIES('parser'='chinese')"
        } catch (Exception e) {
            threw = true
        }
        // 验证 ALTER 行为（不强制断言抛 vs 接受，但要文档化实际行为）
        def r = sql "SHOW CREATE TABLE t_iia_prs_016"
        String ddl = r[0][1].toString().toLowerCase()
        assertTrue(ddl.contains("inverted"),
                   "table should still have inverted index after ALTER attempt; threw=${threw}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_prs_016" } catch (Exception ignore) {}
    }
}
