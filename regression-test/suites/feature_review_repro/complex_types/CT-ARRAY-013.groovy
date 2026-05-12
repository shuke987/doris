// CT-ARRAY-013: ARRAY of HLL → 拒绝
suite("repro_ct_array_013") {
    sql "DROP TABLE IF EXISTS t_ct_array_013"
    boolean threw = false
    String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_013 (id INT, a ARRAY<HLL>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) {
        threw = true; err = e.toString()
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_013" } catch (Exception ignore) {}
    }
    assertTrue(threw, "CT-ARRAY-013: ARRAY<HLL> must be rejected; threw=${threw} err=${err}")
}
