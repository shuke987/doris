// JT-CAST-007: CAST CHAR(N) AS JSONB
suite("repro_jt_cast_007") {
    def r = sql "SELECT CAST(CAST('{\"a\":1}' AS CHAR(50)) AS JSONB)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"a\":1"), "JT-CAST-007; observed=${r}")
}
