// JT-BASE-033 (HARD RULE): ALTER VARCHAR → JSONB MUST succeed per cast matrix
suite("repro_jt_base_033") {
    sql "DROP TABLE IF EXISTS t_jt_base_033"
    try {
        sql """
            CREATE TABLE t_jt_base_033 (id INT, s VARCHAR(100))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        boolean threw = false
        try { sql "ALTER TABLE t_jt_base_033 MODIFY COLUMN s JSONB" }
        catch (Exception e) { threw = true }
        assertEquals(false, threw, "ALTER VARCHAR → JSONB MUST succeed per cast matrix; threw=${threw}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_033" } catch (Exception ignore) {}
    }
}
