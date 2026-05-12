// CT-ARRAY-018: ARRAY 不可作 UNIQUE key
suite("repro_ct_array_018") {
    sql "DROP TABLE IF EXISTS t_ct_array_018"
    boolean threw = false
    String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_018 (a ARRAY<INT>, b INT)
            UNIQUE KEY(a) DISTRIBUTED BY HASH(a) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) {
        threw = true; err = e.toString()
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_018" } catch (Exception ignore) {}
    }
    assertTrue(threw, "CT-ARRAY-018: ARRAY cannot be UNIQUE key; threw=${threw} err=${err}")
}
