// CT-ARRAY-032: ARRAY 列名含中文
suite("repro_ct_array_032") {
    sql "DROP TABLE IF EXISTS t_ct_array_032"
    try {
        sql """
            CREATE TABLE t_ct_array_032 (id INT, `数据` ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_array_032 VALUES (1, [10,20])"
        def r = sql "SELECT `数据` FROM t_ct_array_032"
        assertEquals(1, r.size(), "CT-ARRAY-032: Chinese column name works; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_032" } catch (Exception ignore) {}
    }
}
