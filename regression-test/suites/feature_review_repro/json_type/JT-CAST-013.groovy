// JT-CAST-013: object → string
suite("repro_jt_cast_013") {
    def r = sql "SELECT CAST(CAST('{\"a\":1}' AS JSONB) AS STRING)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"a\":1"), "JT-CAST-013; observed=${r}")
}
