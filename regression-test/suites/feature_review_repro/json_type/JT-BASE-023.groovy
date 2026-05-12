// JT-BASE-023: SHOW CREATE TABLE 显示 JSON 还是 JSONB
suite("repro_jt_base_023") {
    sql "DROP TABLE IF EXISTS t_jt_base_023"
    try {
        sql """
            CREATE TABLE t_jt_base_023 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "SHOW CREATE TABLE t_jt_base_023"
        String def_str = r[0][1].toString().toLowerCase()
        assertTrue(def_str.contains("json") || def_str.contains("jsonb"),
            "JT-BASE-023; observed=${def_str.take(200)}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_023" } catch (Exception ignore) {}
    }
}
