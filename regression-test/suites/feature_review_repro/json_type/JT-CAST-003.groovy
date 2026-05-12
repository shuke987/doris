// JT-CAST-003: CAST NULL AS JSONB
suite("repro_jt_cast_003") {
    def r = sql "SELECT CAST(NULL AS JSONB)"
    assertEquals(null, r[0][0], "JT-CAST-003; observed=${r}")
}
