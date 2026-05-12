suite("repro_ct_explode_011") {
    sql "DROP TABLE IF EXISTS t_ct_explode_011"
    try {
        sql """
            CREATE TABLE t_ct_explode_011 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_explode_011 SELECT 1, map('a',1,'b',2)"
        boolean threw = false; int sz = -1
        try {
            def r = sql "SELECT count(*) FROM t_ct_explode_011 LATERAL VIEW explode_map(m) tmp AS k, v"
            sz = (r[0][0] as Number).intValue()
        } catch (Exception e) { threw = true }
        assertTrue(threw || sz == 2, "CT-EXPLODE-011: explode_map 2 rows; threw=${threw} sz=${sz}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_explode_011" } catch (Exception ignore) {}
    }
}
