// JT-CAST-047: 空 ARRAY → JSONB
suite("repro_jt_cast_047") {
    def r = sql "SELECT CAST(ARRAY() AS JSONB)"
    assertEquals("[]", r[0][0].toString(), "JT-CAST-047; observed=${r}")
}
