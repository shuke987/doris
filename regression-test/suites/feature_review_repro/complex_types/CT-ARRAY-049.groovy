// CT-ARRAY-049: MODIFY ARRAY<ARRAY<INT>> -> ARRAY<INT> (降维)
suite("repro_ct_array_049") {
    sql "DROP TABLE IF EXISTS t_ct_array_049"
    try {
        sql """
            CREATE TABLE t_ct_array_049 (id INT, a ARRAY<ARRAY<INT>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        boolean threw = false; String err = ""
        try {
            sql "ALTER TABLE t_ct_array_049 MODIFY COLUMN a ARRAY<INT>"
        } catch (Exception e) { threw = true; err = e.toString() }
        assertTrue(threw, "CT-ARRAY-049: MODIFY ARRAY<ARRAY<INT>> -> ARRAY<INT> must reject; threw=${threw} err=${err}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_049" } catch (Exception ignore) {}
    }
}
