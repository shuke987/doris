suite("repro_ct_struct_068") {
    sql "DROP TABLE IF EXISTS t_ct_struct_068"
    try {
        sql """
            CREATE TABLE t_ct_struct_068 (id INT, s STRUCT<a:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_struct_068 SELECT 1, named_struct('a',1)"
        // Test s.A case behavior (SEV-2 #N7)
        boolean threw = false
        Object obs = "UNKNOWN"
        try {
            def r = sql "SELECT s.A FROM t_ct_struct_068 WHERE id=1"
            obs = r[0][0]
        } catch (Exception e) { threw = true }
        // SEV-2 #N7: FE may allow case-insensitive, BE may differ
        assertTrue(threw || obs != null, "CT-STRUCT-068: case access; threw=${threw} obs=${obs}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_struct_068" } catch (Exception ignore) {}
    }
}
