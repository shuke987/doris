// JT-EXTRACT-035: 1000 行 vec extract
suite("repro_jt_extract_035") {
    sql "DROP TABLE IF EXISTS t_jt_extract_035"
    try {
        sql """
            CREATE TABLE t_jt_extract_035 (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def values = (1..200).collect { "(${it},'{\"a\":${it}}')" }.join(",")
        sql "INSERT INTO t_jt_extract_035 VALUES ${values}"
        def r = sql "SELECT count(*) FROM t_jt_extract_035 WHERE jsonb_extract_int(j, '\$.a') > 100"
        assertEquals("100", r[0][0].toString(),
            "JT-EXTRACT-035: 200 rows, 100 with a>100; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_extract_035" } catch (Exception ignore) {}
    }
}
