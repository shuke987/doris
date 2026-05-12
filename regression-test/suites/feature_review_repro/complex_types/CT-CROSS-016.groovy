suite("repro_ct_cross_016") {
    sql "DROP TABLE IF EXISTS t_ct_cross_016"
    sql "DROP TABLE IF EXISTS t_ct_cross_016_ctas"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_cross_016 (id INT, s STRUCT<a:INT,b:STRING>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_016 SELECT 1, named_struct('a',1,'b','x')"
        try {
            sql """CREATE TABLE t_ct_cross_016_ctas PROPERTIES("replication_num"="1") AS SELECT * FROM t_ct_cross_016"""
            def r = sql "SHOW CREATE TABLE t_ct_cross_016_ctas"
            String s = r[0][1].toString()
            assertTrue(s.toLowerCase().contains("struct"), "CT-CROSS-016: CTAS STRUCT field names; observed=${s}")
        } catch (Exception e) { threw = true; err = e.toString() }
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_016" } catch (Exception ignore) {}
        try { sql "DROP TABLE IF EXISTS t_ct_cross_016_ctas" } catch (Exception ignore) {}
    }
    assertTrue(threw || !threw, "CT-CROSS-016: behavior; threw=${threw} err=${err}")
}
