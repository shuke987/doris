// JT-BASE-037: MODIFY COLUMN JSONB → INT 应拒绝
suite("repro_jt_base_037") {
    sql "DROP TABLE IF EXISTS t_jt_base_037"
    try {
        sql """
            CREATE TABLE t_jt_base_037 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        boolean threw = false
        try { sql "ALTER TABLE t_jt_base_037 MODIFY COLUMN j INT" }
        catch (Exception e) { threw = true }
        assertTrue(threw, "JT-BASE-037: MODIFY JSONB→INT should be rejected (cast matrix)")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_037" } catch (Exception ignore) {}
    }
}
