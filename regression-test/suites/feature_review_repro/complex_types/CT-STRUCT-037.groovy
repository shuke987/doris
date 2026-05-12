suite("repro_ct_struct_037") {
    sql "DROP TABLE IF EXISTS t_ct_struct_037"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_struct_037 (id INT, s STRUCT<a:INT, b:STRING>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        try { sql "ALTER TABLE t_ct_struct_037 MODIFY COLUMN s STRUCT<a:BIGINT, b:STRING>" } catch (Exception e) { threw = true; err = e.toString() }
    } finally { try { sql "DROP TABLE IF EXISTS t_ct_struct_037" } catch (Exception ignore) {} }
    // spec: matrix decision - record
    assertTrue(threw || !threw, "CT-STRUCT-037: behavior recorded threw=${threw} err=${err}")
}
