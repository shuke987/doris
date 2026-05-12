// JT-BASE-002: JSON 别名建表
suite("repro_jt_base_002") {
    sql "DROP TABLE IF EXISTS t_jt_base_002"
    try {
        sql """
            CREATE TABLE t_jt_base_002 (id INT, j JSON)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "DESC t_jt_base_002"
        boolean found = false
        for (def row : r) {
            if (row[0].toString() == "j") {
                String type = row[1].toString().toLowerCase()
                found = type.contains("json")
            }
        }
        assertTrue(found, "JT-BASE-002: JSON should be alias of JSONB; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_002" } catch (Exception ignore) {}
    }
}
