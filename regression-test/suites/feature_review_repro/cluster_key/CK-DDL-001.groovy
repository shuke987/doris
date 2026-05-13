// CK-DDL-001: SHOW CREATE TABLE 输出 ORDER BY 子句
suite("repro_ck_ddl_001") {
    sql "DROP TABLE IF EXISTS t_ck_ddl_001"
    try {
        sql """
            CREATE TABLE t_ck_ddl_001 (id BIGINT, c1 BIGINT, c2 DATETIME, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (c1, c2)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        def r = sql "SHOW CREATE TABLE t_ck_ddl_001"
        String ddl = r[0][1].toString()
        // ORDER BY 子句应在 SHOW CREATE TABLE 输出中
        assertTrue(ddl.contains("ORDER BY"), "DDL should include ORDER BY clause")
        assertTrue(ddl.indexOf("`c1`") > 0 && ddl.indexOf("`c2`") > 0,
                   "DDL should include cluster key columns; got=${ddl.take(400)}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_ddl_001" } catch (Exception ignore) {}
    }
}
