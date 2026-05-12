// CT-ARRAY-026: ARRAY 作 DISTRIBUTED BY HASH
suite("repro_ct_array_026") {
    sql "DROP TABLE IF EXISTS t_ct_array_026"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_026 (id INT, a ARRAY<INT>)
            DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(a) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) { threw = true; err = e.toString() }
    finally { try { sql "DROP TABLE IF EXISTS t_ct_array_026" } catch (Exception ignore) {} }
    assertTrue(threw, "CT-ARRAY-026: ARRAY cannot be HASH bucket column; threw=${threw} err=${err}")
}
