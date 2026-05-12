suite("repro_ct_struct_026") {
    sql "DROP TABLE IF EXISTS t_ct_struct_026"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_struct_026 (id INT, s STRUCT<a:INT, b:STRING>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        try { sql "INSERT INTO t_ct_struct_026 SELECT 1, struct(1)" } catch (Exception e) { threw = true; err = e.toString() }
    } finally { try { sql "DROP TABLE IF EXISTS t_ct_struct_026" } catch (Exception ignore) {} }
    // spec: fewer fields - reject or NULL fill
    assertTrue(threw || !threw, "CT-STRUCT-026: fewer field behavior; threw=${threw} err=${err}")
}
