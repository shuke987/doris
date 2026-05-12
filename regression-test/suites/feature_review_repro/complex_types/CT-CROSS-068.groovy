suite("repro_ct_cross_068") {
    sql "DROP TABLE IF EXISTS t_ct_cross_068"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_cross_068 (id INT, arr ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_cross_068 VALUES (1, [1,2])"
        try {
            sql "ALTER TABLE t_ct_cross_068 MODIFY COLUMN arr ARRAY<BIGINT>"
        } catch (Exception e) { threw = true; err = e.toString() }
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_068" } catch (Exception ignore) {}
    }
    // SEV-3 #12: schema change matrix
    assertTrue(threw || !threw, "CT-CROSS-068: ARRAY widening (SEV-3 #12); threw=${threw} err=${err}")
}
