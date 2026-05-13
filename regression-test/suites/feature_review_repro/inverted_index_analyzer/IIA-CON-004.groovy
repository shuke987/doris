// IIA-CON-004: lower_case=false → 索引/查询都不归一化，大小写敏感
suite("repro_iia_con_004") {
    sql "DROP TABLE IF EXISTS t_iia_con_004"
    try {
        sql """
            CREATE TABLE t_iia_con_004 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english', 'lower_case'='false'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_con_004 VALUES (1,'ABC'),(2,'abc')"
        // 大小写敏感：MATCH 'ABC' 只命中 id=1; MATCH 'abc' 只命中 id=2
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_con_004 WHERE c MATCH 'ABC'")[0][0],
                     "lower_case=false: MATCH 'ABC' should hit only id=1")
        assertEquals(1L, sql("SELECT count(*) FROM t_iia_con_004 WHERE c MATCH 'abc'")[0][0],
                     "lower_case=false: MATCH 'abc' should hit only id=2")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_con_004" } catch (Exception ignore) {}
    }
}
