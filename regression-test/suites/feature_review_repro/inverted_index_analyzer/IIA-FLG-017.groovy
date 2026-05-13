// IIA-FLG-017: 显式 support_phrase=false 不被默认覆盖
suite("repro_iia_flg_017") {
    sql "DROP TABLE IF EXISTS t_iia_flg_017"
    try {
        sql """
            CREATE TABLE t_iia_flg_017 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english','support_phrase'='false'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        def ddl = sql "SHOW CREATE TABLE t_iia_flg_017"
        String s = ddl[0][1].toString().toLowerCase()
        assertTrue(s.contains('"support_phrase" = "false"') || s.contains('support_phrase=false') || s.contains("support_phrase\" = \"false"),
                   "explicit support_phrase=false should NOT be overridden by default; DDL=${s.take(400)}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_flg_017" } catch (Exception ignore) {}
    }
}
