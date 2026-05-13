// CK-BUILD-001: 端到端 INSERT + SELECT
suite("repro_ck_build_001") {
    sql "DROP TABLE IF EXISTS t_ck_build_001"
    try {
        sql """
            CREATE TABLE t_ck_build_001 (user_id BIGINT, event_time DATETIME, payload STRING)
            UNIQUE KEY (user_id)
            ORDER BY (event_time)
            DISTRIBUTED BY HASH(user_id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        sql """INSERT INTO t_ck_build_001 VALUES
            (1, '2026-01-01 10:00:00', 'a'),
            (2, '2026-01-01 11:00:00', 'b'),
            (1, '2026-01-01 12:00:00', 'c')"""
        def r = sql "SELECT count(*) FROM t_ck_build_001"
        assertEquals(2L, r[0][0], "MOW unique should dedupe by user_id")
        def r2 = sql "SELECT payload FROM t_ck_build_001 WHERE user_id=1"
        assertEquals('c', r2[0][0], "user_id=1 should have latest payload 'c'")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_build_001" } catch (Exception ignore) {}
    }
}
