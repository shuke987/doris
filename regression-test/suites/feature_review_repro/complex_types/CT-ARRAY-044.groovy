// CT-ARRAY-044: MODIFY COLUMN ARRAY<INT> -> ARRAY<BIGINT> (SEV-3 #12)
suite("repro_ct_array_044") {
    sql "DROP TABLE IF EXISTS t_ct_array_044"
    boolean modifyOk = true; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_044 (id INT, a ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "light_schema_change"="false")
        """
        sql "INSERT INTO t_ct_array_044 VALUES (1, [1,2,3])"
        try {
            sql "ALTER TABLE t_ct_array_044 MODIFY COLUMN a ARRAY<BIGINT>"
        } catch (Exception e) { modifyOk = false; err = e.toString() }
        // spec: ARRAY<INT> -> ARRAY<BIGINT> should succeed (widening). Behavior assertion
        assertTrue(modifyOk, "CT-ARRAY-044: MODIFY ARRAY<INT>->ARRAY<BIGINT> should succeed (SEV-3 #12); err=${err}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_044" } catch (Exception ignore) {}
    }
}
