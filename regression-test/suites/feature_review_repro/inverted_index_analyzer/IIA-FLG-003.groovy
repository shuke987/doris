// IIA-FLG-003: stopwords 用户自定义列表（实测当前不支持）→ FE reject
suite("repro_iia_flg_003") {
    sql "DROP TABLE IF EXISTS t_iia_flg_003"
    boolean threw = false
    try {
        sql """
            CREATE TABLE t_iia_flg_003 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english','stopwords'='a,b,c'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
    } catch (Exception e) {
        threw = true
        assertTrue(e.getMessage().toLowerCase().contains('stopwords') || e.getMessage().toLowerCase().contains('none'),
                   "Error msg should mention stopwords=none; got=${e.getMessage()}")
    }
    assertTrue(threw, "FE rejects custom stopwords list (only 'none' supported, function gap)")
    sql "DROP TABLE IF EXISTS t_iia_flg_003"
}
