// CK-INT-004: cluster key + sequence column
suite("repro_ck_int_004") {
    sql "DROP TABLE IF EXISTS t_ck_int_004"
    try {
        sql """
            CREATE TABLE t_ck_int_004 (id BIGINT, v BIGINT, ts BIGINT, payload STRING)
            UNIQUE KEY (id)
            ORDER BY (v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true",
                        "function_column.sequence_col"="ts")
        """
        sql "INSERT INTO t_ck_int_004 VALUES (1, 10, 100, 'a'), (1, 20, 50, 'b')"
        def r = sql "SELECT payload FROM t_ck_int_004 WHERE id=1"
        assertEquals('a', r[0][0], "sequence col should keep payload='a' (ts=100 > ts=50)")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_int_004" } catch (Exception ignore) {}
    }
}
