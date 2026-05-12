// JT-CAST-024: jsonb T_Bool → BOOLEAN
suite("repro_jt_cast_024") {
    def r = sql "SELECT CAST(CAST('true' AS JSONB) AS BOOLEAN)"
    String v = r[0][0].toString().toLowerCase()
    assertTrue(v == "true" || v == "1", "JT-CAST-024; observed=${r}")
}
