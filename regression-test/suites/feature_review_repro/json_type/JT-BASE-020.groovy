// JT-BASE-020: AUTO PARTITION BY date_trunc(j, 'day') 应拒绝
suite("repro_jt_base_020") {
    sql "DROP TABLE IF EXISTS t_jt_base_020"
    try {
        boolean threw = false
        try {
            sql """
                CREATE TABLE t_jt_base_020 (id INT, j JSONB)
                DUPLICATE KEY(id)
                AUTO PARTITION BY RANGE (date_trunc(j, 'day')) ()
                DISTRIBUTED BY HASH(id) BUCKETS 1
                PROPERTIES("replication_num"="1")
            """
        } catch (Exception e) { threw = true }
        assertTrue(threw, "JT-BASE-020: JSONB cannot be auto partition expr")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_020" } catch (Exception ignore) {}
    }
}
