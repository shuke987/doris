// JT-BASE-001: 普通 JSONB 列建表
suite("repro_jt_base_001") {
    sql "DROP TABLE IF EXISTS t_jt_base_001"
    try {
        sql """
            CREATE TABLE t_jt_base_001 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "DESC t_jt_base_001"
        boolean found = false
        for (def row : r) {
            if (row[0].toString() == "j") {
                String type = row[1].toString().toLowerCase()
                found = (type.contains("json"))
            }
        }
        assertTrue(found, "JT-BASE-001: JSONB column should exist; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_001" } catch (Exception ignore) {}
    }
}
