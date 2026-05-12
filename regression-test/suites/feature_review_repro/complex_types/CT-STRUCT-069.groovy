suite("repro_ct_struct_069") {
    sql "DROP TABLE IF EXISTS t_ct_struct_069"
    try {
        sql """
            CREATE TABLE t_ct_struct_069 (id INT, s STRUCT<a:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_struct_069 SELECT 1, named_struct('a',1)"
        boolean threw = false; String err = ""
        try { sql "SELECT s.missing FROM t_ct_struct_069" } catch (Exception e) { threw = true; err = e.toString() }
        assertTrue(threw, "CT-STRUCT-069: missing dot access reject; threw=${threw} err=${err}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_069" } catch (Exception ignore) {}
    }
}
