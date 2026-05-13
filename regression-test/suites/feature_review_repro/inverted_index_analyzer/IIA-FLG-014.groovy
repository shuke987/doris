// IIA-FLG-014: 多 flag 默认注入 (Index.java:78-97)
// 实测：当 parser/analyzer/normalizer 任一指定时，support_phrase + lower_case 自动注入 = true
suite("repro_iia_flg_014") {
    sql "DROP TABLE IF EXISTS t_iia_flg_014"
    try {
        sql """
            CREATE TABLE t_iia_flg_014 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        def r = sql "SHOW CREATE TABLE t_iia_flg_014"
        String ddl = r[0][1].toString().toLowerCase()
        assertTrue(ddl.contains('"support_phrase" = "true"') || ddl.contains("support_phrase = true") || ddl.contains("support_phrase\" = \"true"),
                   "specifying parser should auto-inject support_phrase=true; DDL=${ddl.take(300)}")
        assertTrue(ddl.contains('"lower_case" = "true"') || ddl.contains("lower_case = true"),
                   "specifying parser should auto-inject lower_case=true; DDL=${ddl.take(300)}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_flg_014" } catch (Exception ignore) {}
    }
}
