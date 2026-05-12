// CT-ARRAY-015: ARRAY of VARIANT - 行为断言
suite("repro_ct_array_015") {
    sql "DROP TABLE IF EXISTS t_ct_array_015"
    boolean threw = false
    String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_015 (id INT, a ARRAY<VARIANT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) {
        threw = true; err = e.toString()
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_015" } catch (Exception ignore) {}
    }
    assertTrue(threw, "CT-ARRAY-015: ARRAY<VARIANT> behavior assertion (spec gap); threw=${threw} err=${err}")
}
