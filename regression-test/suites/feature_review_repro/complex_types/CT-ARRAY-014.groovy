// CT-ARRAY-014: ARRAY of JSONB - 行为断言
suite("repro_ct_array_014") {
    sql "DROP TABLE IF EXISTS t_ct_array_014"
    boolean threw = false
    String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_014 (id INT, a ARRAY<JSONB>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) {
        threw = true; err = e.toString()
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_014" } catch (Exception ignore) {}
    }
    // spec gap pre-step5.2: behavior assertion - either rejected or supported
    // Current behavior: usually rejected; assert one way and observe
    assertTrue(threw, "CT-ARRAY-014: ARRAY<JSONB> behavior assertion (spec gap); threw=${threw} err=${err}")
}
