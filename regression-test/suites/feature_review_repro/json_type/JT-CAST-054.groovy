// JT-CAST-054: BOOLEAN → JSONB
suite("repro_jt_cast_054") {
    def r = sql "SELECT CAST(true AS JSONB)"
    assertEquals("true", r[0][0].toString(), "JT-CAST-054; observed=${r}")
}
