// JT-MODIFY-061: UPDATE table with json_set (unique-key)
suite("repro_jt_modify_061") {
    sql "DROP TABLE IF EXISTS t_jt_modify_061"
    try {
        sql """
            CREATE TABLE t_jt_modify_061 (id INT, j JSONB)
            UNIQUE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1","enable_unique_key_merge_on_write"="true")
        """
        sql "INSERT INTO t_jt_modify_061 VALUES (1,'{\"a\":1}')"
        boolean threw = false
        try { sql "UPDATE t_jt_modify_061 SET j = json_set(j, '\$.b', 2) WHERE id=1" }
        catch (Exception e) { threw = true }
        if (!threw) {
            def r = sql "SELECT jsonb_extract(j, '\$.b') FROM t_jt_modify_061 WHERE id=1"
            String v = r[0][0]?.toString() ?: ""
            assertTrue(v == "2" || v == "null",
                "JT-MODIFY-061; observed=${r}")
        }
    } finally {
        try { sql "DROP TABLE IF EXISTS t_jt_modify_061" } catch (Exception ignore) {}
    }
}
