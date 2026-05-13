// IIA-CON-002: parser=chinese 查询拆词
// 注意：coarse_grained 倾向长 token；用 dict 中**不连续**的中文确保切词
suite("repro_iia_con_002") {
    sql "DROP TABLE IF EXISTS t_iia_con_002"
    try {
        sql """
            CREATE TABLE t_iia_con_002 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='chinese','parser_mode'='coarse_grained'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        // coarse '我爱北京' → [我, 爱, 北京]（北京独立 token）
        // coarse '清华很美' → [清华, 很, 美]
        sql "INSERT INTO t_iia_con_002 VALUES (1,'我爱北京'),(2,'清华很美')"
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_con_002 WHERE c MATCH '北京'")[0][0],
                     "MATCH '北京' should hit id=1")
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_con_002 WHERE c MATCH '清华'")[0][0],
                     "MATCH '清华' should hit id=2")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_con_002" } catch (Exception ignore) {}
    }
}
