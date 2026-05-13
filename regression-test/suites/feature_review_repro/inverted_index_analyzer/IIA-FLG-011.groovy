// IIA-FLG-011: ignore_above=1024 自定义配置可设 + DDL 持久化
// 注：与 FLG-010 同一发现，MATCH count 不能区分 indexed vs runtime fallback。
// 此 case 验证 DDL 接受 + 不 crash。
suite("repro_iia_flg_011") {
    sql "DROP TABLE IF EXISTS t_iia_flg_011"
    try {
        sql """
            CREATE TABLE t_iia_flg_011 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('ignore_above'='1024'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        def ddl = sql "SHOW CREATE TABLE t_iia_flg_011"
        assertTrue(ddl[0][1].toString().contains("\"ignore_above\" = \"1024\""),
                   "DDL should persist ignore_above=1024; got=${ddl[0][1].toString().take(300)}")
        String s800 = 'y' * 800
        sql "INSERT INTO t_iia_flg_011 VALUES (1,'${s800}'),(2,'short')"
        assertEquals(2L, sql("SELECT count(*) FROM t_iia_flg_011")[0][0],
                     "both rows should be inserted")
        // 800-byte 在 ignore_above=1024 内，应被索引
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_flg_011 WHERE c MATCH '${s800}'")[0][0],
                     "800-byte string under ignore_above=1024 should be indexed/matchable")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_flg_011" } catch (Exception ignore) {}
    }
}
