// CT-ARRAY-012: ARRAY of BITMAP → 拒绝
suite("repro_ct_array_012") {
    sql "DROP TABLE IF EXISTS t_ct_array_012"
    boolean threw = false
    String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_012 (id INT, a ARRAY<BITMAP>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) {
        threw = true; err = e.toString()
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_012" } catch (Exception ignore) {}
    }
    assertTrue(threw, "CT-ARRAY-012: ARRAY<BITMAP> must be rejected; threw=${threw} err=${err}")
}
