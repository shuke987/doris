// CT-ARRAY-035: information_schema 查 ARRAY data_type
suite("repro_ct_array_035") {
    sql "DROP TABLE IF EXISTS t_ct_array_035"
    try {
        sql """
            CREATE TABLE t_ct_array_035 (id INT, a ARRAY<INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql """SELECT data_type FROM information_schema.columns WHERE table_name='t_ct_array_035' AND column_name='a'"""
        assertTrue(r.size() >= 1, "CT-ARRAY-035: information_schema row exists; observed=${r}")
        String dt = r[0][0].toString().toLowerCase()
        assertTrue(dt.contains("array"), "CT-ARRAY-035: data_type contains 'array'; observed=${dt}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_array_035" } catch (Exception ignore) {}
    }
}
