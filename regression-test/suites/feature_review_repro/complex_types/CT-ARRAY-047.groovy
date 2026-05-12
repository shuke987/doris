// CT-ARRAY-047: MODIFY ARRAY<INT> -> INT (dimension reduction) - reject
suite("repro_ct_array_047") {
    sql "DROP TABLE IF EXISTS t_ct_array_047"
    try {
        sql """
            CREATE TABLE t_ct_array_047 (id INT, a ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        boolean threw = false; String err = ""
        try {
            sql "ALTER TABLE t_ct_array_047 MODIFY COLUMN a INT"
        } catch (Exception e) { threw = true; err = e.toString() }
        assertTrue(threw, "CT-ARRAY-047: MODIFY ARRAY -> INT must reject; threw=${threw} err=${err}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_047" } catch (Exception ignore) {}
    }
}
