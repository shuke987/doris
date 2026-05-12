// JT-BASE-004: NOT NULL JSONB + INSERT NULL → 拒绝
suite("repro_jt_base_004") {
    sql "DROP TABLE IF EXISTS t_jt_base_004"
    try {
        sql """
            CREATE TABLE t_jt_base_004 (id INT, j JSONB NOT NULL)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        boolean threw = false; String err = ""
        try { sql "INSERT INTO t_jt_base_004 VALUES (1, NULL)" }
        catch (Exception e) { threw = true; err = e.message ?: "" }
        assertTrue(threw, "JT-BASE-004: INSERT NULL into NOT NULL JSONB should fail; no error thrown")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_004" } catch (Exception ignore) {}
    }
}
