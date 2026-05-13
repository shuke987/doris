// AGG-COMB-001: BITMAP_UNION combinator on per-row to_bitmap
suite("repro_agg_comb_001") {
    sql "DROP TABLE IF EXISTS t_agg_comb_001"
    try {
        sql """CREATE TABLE t_agg_comb_001 (id INT, v BIGINT) DUPLICATE KEY(id)
            DISTRIBUTED BY HASH(id) BUCKETS 1 PROPERTIES("replication_num"="1")"""
        sql "INSERT INTO t_agg_comb_001 VALUES (1,100),(2,200),(3,100),(4,300)"
        def r = sql """SELECT
            BITMAP_COUNT(BITMAP_UNION(to_bitmap(v))),
            BITMAP_UNION_COUNT(to_bitmap(v))
            FROM t_agg_comb_001"""
        // BITMAP_UNION_COUNT shortcut = BITMAP_COUNT(BITMAP_UNION(...))
        assertEquals(r[0][0], r[0][1],
            "BITMAP_UNION_COUNT = BITMAP_COUNT(BITMAP_UNION); ${r[0][0]} vs ${r[0][1]}")
    } finally {
        try { sql "DROP TABLE IF EXISTS t_agg_comb_001" } catch (Exception ignore) {}
    }
}
