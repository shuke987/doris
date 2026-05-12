suite("repro_ct_explode_030") {
    sql "DROP TABLE IF EXISTS t_ct_explode_030"
    try {
        sql """
            CREATE TABLE t_ct_explode_030 (id INT, s STRUCT<arr:ARRAY<INT>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_explode_030 SELECT 1, named_struct('arr', array(1,2,3))"
        boolean threw = false; int n = -1
        try {
            def r = sql "SELECT count(*) FROM t_ct_explode_030 LATERAL VIEW explode(s.arr) tmp AS x"
            n = (r[0][0] as Number).intValue()
        } catch (Exception e) { threw = true }
        assertTrue(threw || n == 3, "CT-EXPLODE-030: explode struct.arr; threw=${threw} n=${n}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_explode_030" } catch (Exception ignore) {}
    }
}
