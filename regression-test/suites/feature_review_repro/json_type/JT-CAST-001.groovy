// JT-CAST-001: CAST '{"a":1}' AS JSONB
suite("repro_jt_cast_001") {
    def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
    assertEquals("{\"a\":1}", r[0][0].toString(), "JT-CAST-001; observed=${r}")
}
