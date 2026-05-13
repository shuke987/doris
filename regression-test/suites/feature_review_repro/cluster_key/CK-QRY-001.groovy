// CK-QRY-001: cluster key 表上 unique key prefix 查询不 crash
// 注：SEV-1 #2 — OlapScanNode 清空 keyColumnNames，prefix scan 退化为全表扫描
// 但 query 结果正确性不受影响，只是性能下降。本 case 锁定**正确性**。
suite("repro_ck_qry_001") {
    sql "DROP TABLE IF EXISTS t_ck_qry_001"
    try {
        sql """
            CREATE TABLE t_ck_qry_001 (a BIGINT, b BIGINT, c BIGINT, payload STRING)
            UNIQUE KEY (a)
            ORDER BY (b, c)
            DISTRIBUTED BY HASH(a) BUCKETS 1
            PROPERTIES ("replication_num"="1", "enable_unique_key_merge_on_write"="true")
        """
        sql "INSERT INTO t_ck_qry_001 VALUES (1,10,100,'p1'),(2,20,200,'p2'),(3,30,300,'p3')"
        // unique key (a) 谓词
        assertEquals(1L, sql("SELECT count(*) FROM t_ck_qry_001 WHERE a=2")[0][0],
                     "unique key prefix query result correctness")
        // cluster key (b, c) 谓词
        assertEquals(1L, sql("SELECT count(*) FROM t_ck_qry_001 WHERE b=20")[0][0],
                     "cluster key column query result correctness")
        // 范围
        assertEquals(2L, sql("SELECT count(*) FROM t_ck_qry_001 WHERE b BETWEEN 20 AND 30")[0][0],
                     "cluster key range result correctness")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ck_qry_001" } catch (Exception ignore) {}
    }
}
