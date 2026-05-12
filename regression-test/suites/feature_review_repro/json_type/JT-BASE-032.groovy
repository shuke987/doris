// JT-BASE-032: DROP COLUMN JSONB
suite("repro_jt_base_032") {
    sql "DROP TABLE IF EXISTS t_jt_base_032"
    try {
        sql """
            CREATE TABLE t_jt_base_032 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1","light_schema_change"="true")
        """
        sql "ALTER TABLE t_jt_base_032 DROP COLUMN j"
        def r = sql "DESC t_jt_base_032"
        boolean has_j = false
        for (def row : r) {
            if (row[0].toString() == "j") has_j = true
        }
        assertFalse(has_j, "JT-BASE-032: j column should be dropped; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_032" } catch (Exception ignore) {}
    }
}
