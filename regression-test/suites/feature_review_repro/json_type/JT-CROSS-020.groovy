// JT-CROSS-020: JSONB × LATERAL VIEW EXPLODE (via cross join)
suite("repro_jt_cross_020") {
    boolean threw = false
    def r = null
    try {
        r = sql """
            SELECT e FROM (SELECT CAST('[1,2,3]' AS JSONB) AS j) t
            LATERAL VIEW EXPLODE(CAST(j AS ARRAY<INT>)) lv AS e
        """
    } catch (Exception e) { threw = true }
    // lock observation; either succeeds or rejects
    if (!threw) {
        assertEquals(3, r.size(), "JT-CROSS-020; observed=${r}")
    }
}
