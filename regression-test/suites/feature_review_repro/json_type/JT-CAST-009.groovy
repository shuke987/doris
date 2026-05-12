// JT-CAST-009: CAST STRING AS JSONB
suite("repro_jt_cast_009") {
    def r = sql "SELECT CAST(CAST('{\"a\":1}' AS STRING) AS JSONB)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"a\":1"), "JT-CAST-009; observed=${r}")
}
