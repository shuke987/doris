// JT-CROSS-013: JSONB × UNION ALL
suite("repro_jt_cross_013") {
    def r = sql """
        SELECT CAST('{\"a\":1}' AS JSONB) AS j
        UNION ALL
        SELECT CAST('{\"b\":2}' AS JSONB)
    """
    assertEquals(2, r.size(), "JT-CROSS-013; observed=${r}")
}
