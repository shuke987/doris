// JT-CAST-016: T_Int64 → string
suite("repro_jt_cast_016") {
    def r = sql "SELECT CAST(CAST('12345' AS JSONB) AS STRING)"
    assertEquals("12345", r[0][0].toString(), "JT-CAST-016; observed=${r}")
}
