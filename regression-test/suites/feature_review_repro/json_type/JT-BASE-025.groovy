// JT-BASE-025: information_schema 查 column type
suite("repro_jt_base_025") {
    sql "DROP TABLE IF EXISTS t_jt_base_025"
    try {
        sql """
            CREATE TABLE t_jt_base_025 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "SELECT data_type FROM information_schema.columns WHERE table_name='t_jt_base_025' AND column_name='j'"
        if (r.size() > 0) {
            String dt = r[0][0].toString().toLowerCase()
            assertTrue(dt.contains("json"), "JT-BASE-025; observed=${r}")
        }
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_025" } catch (Exception ignore) {}
    }
}
