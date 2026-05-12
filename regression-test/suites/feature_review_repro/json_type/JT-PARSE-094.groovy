// JT-PARSE-094: INSERT vs SELECT 行为应一致
suite("repro_jt_parse_094") {
    sql "DROP TABLE IF EXISTS t_jt_parse_094_src"
    sql "DROP TABLE IF EXISTS t_jt_parse_094_tgt"
    try {
        sql """
            CREATE TABLE t_jt_parse_094_src (id INT, s VARCHAR(1000))
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql """
            CREATE TABLE t_jt_parse_094_tgt (id INT, j JSONB)
            DUPLICATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_jt_parse_094_src VALUES (1,'{\"a\":1}'),(2,'{a:'),(3,'{\"b\":2}')"
        boolean insert_threw = false, select_threw = false
        try { sql "INSERT INTO t_jt_parse_094_tgt SELECT id, jsonb_parse(s) FROM t_jt_parse_094_src" }
        catch (Exception e) { insert_threw = true }
        try { sql "SELECT jsonb_parse(s) FROM t_jt_parse_094_src" }
        catch (Exception e) { select_threw = true }
        // Both should behave same; if not, SEV
        assertEquals(insert_threw, select_threw,
            "JT-PARSE-094 (SEV-1 #1): INSERT vs SELECT should be consistent; insert_threw=${insert_threw}, select_threw=${select_threw}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_parse_094_src" } catch (Exception ignore) {}
        try { sql "DROP TABLE IF EXISTS t_jt_parse_094_tgt" } catch (Exception ignore) {}
    }
}
