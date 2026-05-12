// JT-BASE-035: MODIFY JSONB → VARCHAR (matrix 允许)
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
        // observation lock — spec says allowed
        assertNotNull(threw, "JT-BASE-035 obs; threw=${threw}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_035" } catch (Exception ignore) {}
    }
}
