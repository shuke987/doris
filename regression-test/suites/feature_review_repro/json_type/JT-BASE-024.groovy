// JT-BASE-024: DESC table JSONB column type 显示
suite("repro_jt_base_024") {
    sql "DROP TABLE IF EXISTS t_jt_base_024"
    try {
        sql """
            CREATE TABLE t_jt_base_024 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "DESC t_jt_base_024"
        String type_j = ""
        for (def row : r) {
            if (row[0].toString() == "j") { type_j = row[1].toString().toUpperCase() }
        }
        assertTrue(type_j == "JSON" || type_j == "JSONB",
            "JT-BASE-024: DESC type for JSONB col should be JSON or JSONB; observed=${type_j}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_024" } catch (Exception ignore) {}
    }
}
