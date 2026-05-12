// JT-BASE-040: light_schema_change + JSONB ADD/DROP
suite("repro_jt_base_040") {
    sql "DROP TABLE IF EXISTS t_jt_base_040"
    try {
        sql """
            CREATE TABLE t_jt_base_040 (id INT)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1","light_schema_change"="true")
        """
        sql "ALTER TABLE t_jt_base_040 ADD COLUMN j JSONB"
        def r = sql "DESC t_jt_base_040"
        boolean has_j = false
        for (def row : r) { if (row[0].toString() == "j") has_j = true }
        assertTrue(has_j, "JT-BASE-040: light schema change ADD JSONB; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_base_040" } catch (Exception ignore) {}
    }
}
