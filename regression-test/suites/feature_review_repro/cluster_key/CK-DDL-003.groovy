// CK-DDL-003: CREATE TABLE LIKE 复制 cluster key
suite("repro_ck_ddl_003") {
    sql "DROP TABLE IF EXISTS t_ck_ddl_003_src"
    sql "DROP TABLE IF EXISTS t_ck_ddl_003_dst"
    try {
        sql """
            CREATE TABLE t_ck_ddl_003_src (id BIGINT, sort_v BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (sort_v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        sql "CREATE TABLE t_ck_ddl_003_dst LIKE t_ck_ddl_003_src"
        def ddl = sql "SHOW CREATE TABLE t_ck_ddl_003_dst"
        assertTrue(ddl[0][1].toString().toLowerCase().contains("order by"),
                   "CREATE TABLE LIKE should copy cluster key; DDL=${ddl[0][1].toString().take(400)}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_ddl_003_src" } catch (Exception ignore) {}
        try { sql "DROP TABLE IF EXISTS t_ck_ddl_003_dst" } catch (Exception ignore) {}
    }
}
