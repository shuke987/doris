// JT-BASE-006: JSONB DEFAULT '{}' 应拒绝（spec §6: 仅 NULL default 允许）
suite("repro_jt_base_006") {
    sql "DROP TABLE IF EXISTS t_jt_base_006"
    try {
        boolean threw = false; String err = ""
        try {
            sql """
                CREATE TABLE t_jt_base_006 (id INT, j JSONB DEFAULT '{}')
                DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
        } catch (Exception e) { threw = true; err = e.message ?: "" }
        assertTrue(threw,
            "JT-BASE-006: JSONB DEFAULT non-NULL should be rejected (spec §6)")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_006" } catch (Exception ignore) {}
    }
}
