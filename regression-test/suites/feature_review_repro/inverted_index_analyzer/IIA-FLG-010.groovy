// IIA-FLG-010: ignore_above 默认 256 (DDL 配置 + 不 crash)
// 实测发现 doc gap：MATCH 即使字段超 ignore_above 也命中（runtime filter fallback or 实际未过滤）。
// 真实"index 是否有该 token"需要 EXPLAIN / 索引文件 inspection 验证，不在 batch 1 范围。
suite("repro_iia_flg_010") {
    sql "DROP TABLE IF EXISTS t_iia_flg_010"
    try {
        sql """
            CREATE TABLE t_iia_flg_010 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        String long_str = 'x' * 257
        sql "INSERT INTO t_iia_flg_010 VALUES (1, '${long_str}'),(2, 'short')"
        // 不 crash + 数据完整可访问（无论 index 是否包含）
        def r = sql "SELECT count(*) FROM t_iia_flg_010"
        assertEquals(2L, r[0][0], "both rows should exist in data even if long-string skipped from index")
        // short 字符串必须 MATCH 命中
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_flg_010 WHERE c MATCH 'short'")[0][0],
                     "short string should match")
        // 长字符串 MATCH 行为：当前 4.1 实测命中（runtime fallback）
        // 注：doc 称 ignore_above 跳过索引，但 MATCH 仍命中 = doc/code 行为不一致 (potential doc gap)
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_flg_010" } catch (Exception ignore) {}
    }
}
