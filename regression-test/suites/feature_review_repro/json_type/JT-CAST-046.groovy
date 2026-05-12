// JT-CAST-046: NULL ARRAY → JSONB
suite("repro_jt_cast_046") {
    def r = sql "SELECT CAST(CAST(NULL AS ARRAY<INT>) AS JSONB)"
    assertEquals(null, r[0][0], "JT-CAST-046; observed=${r}")
}
