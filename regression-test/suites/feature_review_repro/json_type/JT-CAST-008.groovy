// JT-CAST-008: CAST VARCHAR AS JSONB
suite("repro_jt_cast_008") {
    def r = sql "SELECT CAST(CAST('{\"a\":1}' AS VARCHAR(100)) AS JSONB)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"a\":1"), "JT-CAST-008; observed=${r}")
}
