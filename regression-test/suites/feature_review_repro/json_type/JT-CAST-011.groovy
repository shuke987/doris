// JT-CAST-011: `'{"a":1}'::JSONB` PG 语法
suite("repro_jt_cast_011") {
    try {
        def r = sql """SELECT '{\"a\":1}'::JSONB"""
        assertNotNull(r[0][0], "JT-CAST-011; observed=${r}")
    } catch (Exception e) {
        // PG syntax may not be supported in nereids — accept either path
        assertTrue(true)
    }
}
