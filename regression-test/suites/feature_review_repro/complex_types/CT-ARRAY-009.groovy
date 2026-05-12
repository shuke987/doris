// CT-ARRAY-009: ARRAY 嵌套深度 10 层 (>9) → 行为断言
suite("repro_ct_array_009") {
    sql "DROP TABLE IF EXISTS t_ct_array_009"
    boolean threw = false
    String err = ""
    try {
        sql """
            CREATE TABLE t_ct_array_009 (id INT, a ARRAY<ARRAY<ARRAY<ARRAY<ARRAY<ARRAY<ARRAY<ARRAY<ARRAY<ARRAY<INT>>>>>>>>>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
    } catch (Exception e) {
        threw = true
        err = e.toString()
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_009" } catch (Exception ignore) {}
    }
    // spec: should reject with nesting depth limit; record current behavior
    assertTrue(threw, "CT-ARRAY-009: 10-level nested ARRAY should be rejected (spec depth limit 9); threw=${threw} err=${err}")
}
