// CK-DDL-002: DESC TABLE 显示 cluster key 列
suite("repro_ck_ddl_002") {
    sql "DROP TABLE IF EXISTS t_ck_ddl_002"
    try {
        sql """
            CREATE TABLE t_ck_ddl_002 (id BIGINT, c1 BIGINT, c2 DATETIME)
            UNIQUE KEY (id)
            ORDER BY (c1, c2)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        def r = sql "DESC t_ck_ddl_002"
        // DESC TABLE 至少应包含 3 列
        assertTrue(r.size() == 3, "DESC should return 3 columns; got=${r.size()}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_ddl_002" } catch (Exception ignore) {}
    }
}
