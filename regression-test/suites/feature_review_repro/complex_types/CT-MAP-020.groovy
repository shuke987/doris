suite("repro_ct_map_020") {
    sql "DROP TABLE IF EXISTS t_repro_ct_map_020"
    try {
        sql """
            CREATE TABLE t_repro_ct_map_020 (id INT, m MAP<STRING,INT> REPLACE)
            AGGREGATE KEY(id) DISTRIBUTED BY HASH(id) BUCKETS 1
            PROPERTIES("replication_num"="1")
        """
        sql "INSERT INTO t_repro_ct_map_020 VALUES (1, map('a',1)), (1, map('b',2))"
        def r = sql "SELECT m FROM t_repro_ct_map_020 WHERE id=1"
        assertEquals(1, r.size(), "CT-MAP-020: AGG+REPLACE+MAP works; observed=${r}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_repro_ct_map_020" } catch (Exception ignore) {}
    }
}
