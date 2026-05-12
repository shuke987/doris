// JT-CAST-020: T_Null → string "null"
suite("repro_jt_cast_020") {
    def r = sql "SELECT CAST(CAST('null' AS JSONB) AS STRING)"
    assertEquals("null", r[0][0].toString(), "JT-CAST-020; observed=${r}")
}
