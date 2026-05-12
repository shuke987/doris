suite("repro_ct_struct_027") {
    sql "DROP TABLE IF EXISTS t_ct_struct_027"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_struct_027 (id INT, s STRUCT<a:INT, b:STRING>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        try { sql "INSERT INTO t_ct_struct_027 SELECT 1, struct(1, 'x', 2)" } catch (Exception e) { threw = true; err = e.toString() }
    } finally { try { sql "DROP TABLE IF EXISTS t_ct_struct_027" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-STRUCT-027: extra fields reject; threw=${threw} err=${err}")
}
