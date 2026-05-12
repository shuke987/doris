// CT-ARRAY-017: ARRAY 不可作 DUPLICATE key
suite("repro_ct_array_017") {
    sql "DROP TABLE IF EXISTS t_ct_array_017"
    boolean threw = false
    String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_017 (a ARRAY<INT>, b INT)
            DUPLICATE KEY(a) DISTRIBUTED BY HASH(b) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) {
        threw = true; err = e.toString()
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_017" } catch (Exception ignore) {}
    }
    assertTrue(threw, "CT-ARRAY-017: ARRAY cannot be DUPLICATE key; threw=${threw} err=${err}")
}
