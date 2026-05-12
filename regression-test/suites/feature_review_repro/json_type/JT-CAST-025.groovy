// JT-CAST-025: jsonb T_Bool → INT
suite("repro_jt_cast_025") {
    def r = sql "SELECT CAST(CAST('true' AS JSONB) AS INT)"
    assertEquals("1", r[0][0].toString(), "JT-CAST-025; observed=${r}")
}
