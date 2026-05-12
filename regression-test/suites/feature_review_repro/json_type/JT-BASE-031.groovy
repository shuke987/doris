// JT-BASE-031: ALTER ADD COLUMN JSONB DEFAULT '{}' 应拒绝
suite("repro_jt_base_031") {
    sql "DROP TABLE IF EXISTS t_jt_base_031"
    try {
        sql """
            CREATE TABLE t_jt_base_031 (id INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "light_schema_change"="true")
        """
        boolean threw = false
        try { sql "ALTER TABLE t_jt_base_031 ADD COLUMN j JSONB DEFAULT '{}'" }
        catch (Exception e) { threw = true }
        assertTrue(threw, "JT-BASE-031: ADD JSONB DEFAULT non-NULL should be rejected")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_031" } catch (Exception ignore) {}
    }
}
