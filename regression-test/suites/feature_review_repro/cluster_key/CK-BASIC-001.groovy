// CK-BASIC-001: 基本 cluster key 建表 + SHOW CREATE TABLE 持久化
// Workaround SEV-1 #1: cluster key 不能以 unique key 起头，故用 event_time 作为 cluster key
suite("repro_ck_basic_001") {
    sql "DROP TABLE IF EXISTS t_ck_basic_001"
    try {
        sql """
            CREATE TABLE t_ck_basic_001 (user_id BIGINT, event_time DATETIME, payload STRING)
            UNIQUE KEY (user_id)
            ORDER BY (event_time)
            DISTRIBUTED BY HASH(user_id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        def r = sql "SHOW CREATE TABLE t_ck_basic_001"
        String ddl = r[0][1].toString()
        assertTrue(ddl.contains("ORDER BY"), "DDL should preserve ORDER BY")
        assertTrue(ddl.toLowerCase().contains("event_time"), "DDL should preserve cluster key column")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_basic_001" } catch (Exception ignore) {}
    }
}
