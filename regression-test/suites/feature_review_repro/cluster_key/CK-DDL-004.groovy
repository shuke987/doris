// CK-DDL-004: CTAS 不复制 cluster key (CTAS 产生 dup table)
suite("repro_ck_ddl_004") {
    sql "DROP TABLE IF EXISTS t_ck_ddl_004_src"
    sql "DROP TABLE IF EXISTS t_ck_ddl_004_dst"
    try {
        sql """
            CREATE TABLE t_ck_ddl_004_src (id BIGINT, sort_v BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (sort_v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        sql "INSERT INTO t_ck_ddl_004_src VALUES (1,10,'a')"
        // CTAS 默认建 DUP，不会有 ORDER BY
        sql """CREATE TABLE t_ck_ddl_004_dst PROPERTIES ("replication_num"="1") AS SELECT * FROM t_ck_ddl_004_src"""
        def ddl = sql "SHOW CREATE TABLE t_ck_ddl_004_dst"
        // CTAS 默认 DUP，不该有 ORDER BY（CTAS 不复制 cluster key 是预期）
        assertFalse(ddl[0][1].toString().toLowerCase().contains("order by"),
                    "CTAS should NOT copy cluster key (target is DUP table)")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_ddl_004_src" } catch (Exception ignore) {}
        try { sql "DROP TABLE IF EXISTS t_ck_ddl_004_dst" } catch (Exception ignore) {}
    }
}
