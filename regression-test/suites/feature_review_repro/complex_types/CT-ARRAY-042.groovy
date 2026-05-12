// CT-ARRAY-042: ADD COLUMN ARRAY<INT> DEFAULT '[]' - behavior assertion (spec only NULL)
suite("repro_ct_array_042") {
    sql "DROP TABLE IF EXISTS t_ct_array_042"
    boolean threw = false; String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_042 (id INT, x INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "light_schema_change"="true")
        """
        try {
            sql "ALTER TABLE t_ct_array_042 ADD COLUMN a ARRAY<INT> DEFAULT '[]'"
        } catch (Exception e) { threw = true; err = e.toString() }
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_042" } catch (Exception ignore) {}
    }
    assertTrue(threw, "CT-ARRAY-042: ALTER ADD ARRAY DEFAULT '[]' should be rejected (spec: only NULL); threw=${threw} err=${err}")
}
