// JT-CAST-022: jsonb int → INT
suite("repro_jt_cast_022") {
    def r = sql "SELECT CAST(CAST('42' AS JSONB) AS INT)"
    assertEquals("42", r[0][0].toString(), "JT-CAST-022; observed=${r}")
}
