// CT-ARRAY-033: SHOW CREATE TABLE 显示 ARRAY<INT>
suite("repro_ct_array_033") {
    sql "DROP TABLE IF EXISTS t_ct_array_033"
    try {
        sql """
            CREATE TABLE t_ct_array_033 (id INT, a ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "SHOW CREATE TABLE t_ct_array_033"
        String createSql = r[0][1].toString().toLowerCase()
        assertTrue(createSql.contains("array<int"), "CT-ARRAY-033: SHOW CREATE must contain ARRAY<INT>; observed=${createSql}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_033" } catch (Exception ignore) {}
    }
}
