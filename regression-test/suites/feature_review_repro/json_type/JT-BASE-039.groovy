// JT-BASE-039: MODIFY INT → JSONB 应拒绝（cast matrix 不允许）
suite("repro_jt_base_039") {
    sql "DROP TABLE IF EXISTS t_jt_base_039"
    try {
        sql """
            CREATE TABLE t_jt_base_039 (id INT, v INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        boolean threw = false
        try { sql "ALTER TABLE t_jt_base_039 MODIFY COLUMN v JSONB" }
        catch (Exception e) { threw = true }
        assertTrue(threw, "JT-BASE-039: MODIFY INT→JSONB should be rejected (cast matrix)")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_039" } catch (Exception ignore) {}
    }
}
