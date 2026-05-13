// IIA-BND-003: NULL value + inverted index (写入不 crash + 查询行为)
suite("repro_iia_bnd_003") {
    sql "DROP TABLE IF EXISTS t_iia_bnd_003"
    try {
        sql """
            CREATE TABLE t_iia_bnd_003 (id INT, c TEXT,
              INDEX c_idx (c) USING INVERTED PROPERTIES('parser'='english'))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES('replication_num'='1')
        """
        sql "INSERT INTO t_iia_bnd_003 VALUES (1,'hello'),(2,NULL),(3,'world')"
        // NULL 行不应命中任何 MATCH
        def r1 = sql "SELECT count(*) FROM t_iia_bnd_003 WHERE c MATCH 'hello'"
        assertEquals(1L, r1[0][0], "MATCH 'hello' should hit only id=1")
        def r2 = sql "SELECT count(*) FROM t_iia_bnd_003 WHERE c IS NULL"
        assertEquals(1L, r2[0][0], "IS NULL should count id=2")
        // NULL 行不应在 MATCH 'world' 中
        def r3 = sql "SELECT id FROM t_iia_bnd_003 WHERE c MATCH 'world'"
        assertEquals(1, r3.size(), "MATCH 'world' should return only id=3")
        assertEquals(3, r3[0][0], "MATCH 'world' should return id=3 not NULL row")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_iia_bnd_003" } catch (Exception ignore) {}
    }
}
