suite("repro_ct_struct_030") {
    sql "DROP TABLE IF EXISTS t_ct_struct_030"
    try {
        sql """
            CREATE TABLE t_ct_struct_030 (id INT, s STRUCT<a:INT, b:STRING>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        def r = sql "SHOW CREATE TABLE t_ct_struct_030"
        String s = r[0][1].toString().toLowerCase()
        assertTrue(s.contains("struct<"), "CT-STRUCT-030: SHOW CREATE contains STRUCT<; observed=${s}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_030" } catch (Exception ignore) {}
    }
}
