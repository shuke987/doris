// JT-BASE-035 (HARD RULE): ALTER JSONB → VARCHAR MUST succeed
suite("repro_jt_base_035") {
    sql "DROP TABLE IF EXISTS t_jt_base_035"
    try {
        sql """
            CREATE TABLE t_jt_base_035 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        boolean threw = false
        try { sql "ALTER TABLE t_jt_base_035 MODIFY COLUMN j VARCHAR(1000)" }
        catch (Exception e) { threw = true }
        assertEquals(false, threw, "ALTER JSONB → VARCHAR MUST succeed per cast matrix; threw=${threw}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_035" } catch (Exception ignore) {}
    }
}
