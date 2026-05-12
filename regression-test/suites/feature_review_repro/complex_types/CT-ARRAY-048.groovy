// CT-ARRAY-048: MODIFY INT -> ARRAY<INT> (升维) reject
suite("repro_ct_array_048") {
    sql "DROP TABLE IF EXISTS t_ct_array_048"
    try {
        sql """
            CREATE TABLE t_ct_array_048 (id INT, a INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        boolean threw = false; String err = ""
        try {
            sql "ALTER TABLE t_ct_array_048 MODIFY COLUMN a ARRAY<INT>"
        } catch (Exception e) { threw = true; err = e.toString() }
        assertTrue(threw, "CT-ARRAY-048: MODIFY INT -> ARRAY must reject; threw=${threw} err=${err}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_048" } catch (Exception ignore) {}
    }
}
