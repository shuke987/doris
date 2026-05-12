suite("repro_ct_cross_072") {
    sql "DROP TABLE IF EXISTS t_ct_cross_072"
    try {
        sql """
            CREATE TABLE t_ct_cross_072 (id INT, m MAP<STRING,INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1", "light_schema_change"="true")
        """
        sql "ALTER TABLE t_ct_cross_072 DROP COLUMN m"
        assertTrue(true, "CT-CROSS-072: light DROP MAP")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_cross_072" } catch (Exception ignore) {}
    }
}
