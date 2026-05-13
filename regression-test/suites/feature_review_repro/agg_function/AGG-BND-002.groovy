// AGG-BND-002: agg over distinct types
suite("repro_agg_bnd_002") {
    sql "DROP TABLE IF EXISTS t_agg_bnd_002"
    try {
        sql """CREATE TABLE t_agg_bnd_002 (id INT, v TINYINT, w SMALLINT, x BIGINT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_bnd_002 VALUES (1,10,1000,1000000)"
        def r = sql "SELECT SUM(v), SUM(w), SUM(x) FROM t_agg_bnd_002"
        assertNotNull(r[0][0], "SUM(TINYINT)")
        assertNotNull(r[0][1], "SUM(SMALLINT)")
        assertNotNull(r[0][2], "SUM(BIGINT)")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_bnd_002" } catch (Exception ignore) {}
    }
}
