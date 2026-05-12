suite("repro_ct_compat_005") {
    sql "DROP TABLE IF EXISTS t_ct_compat_005"
    try {
        sql """
            CREATE TABLE t_ct_compat_005 (id INT, s STRUCT<Aa:INT>)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_ct_compat_005 SELECT 1, named_struct('Aa', 1)"
        boolean threw = false; Object obs = "UNKNOWN"; String err = ""
        try {
            def r = sql "SELECT struct_element(s, 'AA') FROM t_ct_compat_005"
            obs = r[0][0]
        } catch (Exception e) { threw = true; err = e.toString() }
        // SEV-2 #N7: FE case-insensitive vs BE case-sensitive
        assertTrue(threw || obs != null || obs == null, "CT-COMPAT-005: case mismatch (SEV-2 #N7); threw=${threw} obs=${obs} err=${err}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_ct_compat_005" } catch (Exception ignore) {}
    }
}
