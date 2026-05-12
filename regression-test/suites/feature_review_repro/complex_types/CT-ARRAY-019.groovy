// CT-ARRAY-019: ARRAY 不可作 AGGREGATE key
suite("repro_ct_array_019") {
    sql "DROP TABLE IF EXISTS t_ct_array_019"
    boolean threw = false
    String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_019 (a ARRAY<INT>, b INT SUM)
            AGGREGATE KEY(a) DISTRIBUTED BY HASH(a) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) {
        threw = true; err = e.toString()
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_019" } catch (Exception ignore) {}
    }
    assertTrue(threw, "CT-ARRAY-019: ARRAY cannot be AGGREGATE key; threw=${threw} err=${err}")
}
