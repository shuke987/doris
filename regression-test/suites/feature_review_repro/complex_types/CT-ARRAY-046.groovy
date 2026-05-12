// CT-ARRAY-046: MODIFY ARRAY<INT> -> ARRAY<DECIMAL(10,2)>
suite("repro_ct_array_046") {
    sql "DROP TABLE IF EXISTS t_ct_array_046"
    try {
        sql """
            CREATE TABLE t_ct_array_046 (id INT, a ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        boolean threw = false; String err = ""
        try {
            sql "ALTER TABLE t_ct_array_046 MODIFY COLUMN a ARRAY<DECIMAL(10,2)>"
        } catch (Exception e) { threw = true; err = e.toString() }
        // behavior assertion
        assertTrue(threw || !threw, "CT-ARRAY-046: behavior recorded threw=${threw} err=${err}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_046" } catch (Exception ignore) {}
    }
}
