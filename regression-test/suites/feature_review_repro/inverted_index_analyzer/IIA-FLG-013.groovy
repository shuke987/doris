// IIA-FLG-013: ignore_above 与 parser 互动（doc: ignore_above 是 non-tokenized 限制）
suite("repro_iia_flg_013") {
    sql "DROP TABLE IF EXISTS t_iia_flg_013"
    try {
        sql """
            CREATE TABLE t_iia_flg_013 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english','ignore_above'='10'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        // doc spec: "Specifies the length limit for non-tokenized string indexes (parser not specified)"
        // → 当 parser 指定时 ignore_above 可能不生效；具体行为待验证
        sql "INSERT INTO t_iia_flg_013 VALUES (1,'short one'),(2,'a much longer string than ten bytes for sure')"
        // 实际行为：parser=english 下 ignore_above=10 是否影响索引？
        def r = sql "SELECT count(*) FROM t_iia_flg_013 WHERE c MATCH 'short'"
        // 不强制断言 0 或 1，只确保不 crash + 行为可记录
        assertNotNull(r, "ignore_above + parser combination should not crash; r=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_flg_013" } catch (Exception ignore) {}
    }
}
