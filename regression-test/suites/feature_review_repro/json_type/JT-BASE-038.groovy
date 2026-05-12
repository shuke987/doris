// JT-BASE-038: MODIFY JSONB → ARRAY<INT> 应拒绝
suite("repro_jt_base_038") {
    sql "DROP TABLE IF EXISTS t_jt_base_038"
    try {
        sql """
            CREATE TABLE t_jt_base_038 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        boolean threw = false
        try { sql "ALTER TABLE t_jt_base_038 MODIFY COLUMN j ARRAY<INT>" }
        catch (Exception e) { threw = true }
        assertTrue(threw, "JT-BASE-038: MODIFY JSONB→ARRAY<INT> should be rejected")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_038" } catch (Exception ignore) {}
    }
}
