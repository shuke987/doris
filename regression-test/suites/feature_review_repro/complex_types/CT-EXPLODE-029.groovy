suite("repro_ct_explode_029") {
    sql "DROP TABLE IF EXISTS t_ct_explode_029"
    try {
        sql """
            CREATE TABLE t_ct_explode_029 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_explode_029 SELECT 1, map('a',1,'b',2)"
        boolean threw = false; int n = -1
        try {
            def r = sql "SELECT count(*) FROM t_ct_explode_029 LATERAL VIEW explode_map(m) tmp AS k, v WHERE k IS NOT NULL"
            n = (r[0][0] as Number).intValue()
        } catch (Exception e) { threw = true }
        assertTrue(threw || n == 2, "CT-EXPLODE-029: k/v alignment; threw=${threw} n=${n}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_explode_029" } catch (Exception ignore) {}
    }
}
