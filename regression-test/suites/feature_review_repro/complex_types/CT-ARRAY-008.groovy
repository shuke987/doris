// CT-ARRAY-008: ARRAY 嵌套深度 9 层 (FE 上限)
suite("repro_ct_array_008") {
    sql "DROP TABLE IF EXISTS t_ct_array_008"
    try {
        sql """
            CREATE TABLE t_ct_array_008 (id INT, a ARRAY<ARRAY<ARRAY<ARRAY<ARRAY<ARRAY<ARRAY<ARRAY<ARRAY<INT>>>>>>>>>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "DESC t_ct_array_008"
        assertTrue(r.size() >= 2, "CT-ARRAY-008: 9-level nested ARRAY should be allowed; desc=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_008" } catch (Exception ignore) {}
    }
}
