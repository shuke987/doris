suite("repro_ct_struct_065") {
    sql "DROP TABLE IF EXISTS t_ct_struct_065"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_struct_065 (id INT, s STRUCT<a:INT>, idx INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_struct_065 SELECT 1, struct(10), 1"
        try { sql "SELECT struct_element(s, idx) FROM t_ct_struct_065" } catch (Exception e) { threw = true; err = e.toString() }
    } finally { try { sql "DROP TABLE IF EXISTS t_ct_struct_065" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-STRUCT-065: non-const idx reject; threw=${threw} err=${err}")
}
