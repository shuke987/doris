// CK-INT-002: cluster key + Bloom Filter
suite("repro_ck_int_002") {
    sql "DROP TABLE IF EXISTS t_ck_int_002"
    try {
        sql """
            CREATE TABLE t_ck_int_002 (id BIGINT, v VARCHAR(100), payload STRING)
            UNIQUE KEY (id)
            ORDER BY (v)
            DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true", "bloom_filter_columns"="v")
        """
        sql "INSERT INTO t_ck_int_002 VALUES (1,'aaa','p1'),(2,'bbb','p2')"
        def r = sql "SHOW CREATE TABLE t_ck_int_002"
        String ddl = r[0][1].toString().toLowerCase()
        assertTrue(ddl.contains("order by") && ddl.contains("bloom_filter_columns"),
                   "cluster key + bloom filter should coexist; DDL=${ddl.take(400)}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_int_002" } catch (Exception ignore) {}
    }
}
