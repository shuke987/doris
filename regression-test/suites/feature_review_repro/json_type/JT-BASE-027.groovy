// JT-BASE-027: 多次 ALTER ADD/DROP JSONB columns
suite("repro_jt_base_027") {
    sql "DROP TABLE IF EXISTS t_jt_base_027"
    try {
        sql """
            CREATE TABLE t_jt_base_027 (id INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1","light_schema_change"="true")
        """
        sql "ALTER TABLE t_jt_base_027 ADD COLUMN j1 JSONB"
        sql "ALTER TABLE t_jt_base_027 ADD COLUMN j2 JSONB"
        sql "ALTER TABLE t_jt_base_027 DROP COLUMN j1"
        def r = sql "DESC t_jt_base_027"
        boolean has_j1 = false, has_j2 = false
        for (def row : r) {
            if (row[0].toString() == "j1") has_j1 = true
            if (row[0].toString() == "j2") has_j2 = true
        }
        assertFalse(has_j1, "JT-BASE-027: j1 dropped; desc=${r}")
        assertTrue(has_j2, "JT-BASE-027: j2 still present; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_027" } catch (Exception ignore) {}
    }
}
