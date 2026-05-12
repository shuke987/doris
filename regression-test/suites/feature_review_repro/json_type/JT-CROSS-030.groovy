// JT-CROSS-030: JSONB × prepared (PG-style cast in SELECT)
suite("repro_jt_cross_030") {
    // Use CAST instead of ::
    def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
    assertNotNull(r[0][0], "JT-CROSS-030; observed=${r}")
}
