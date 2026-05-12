// JT-CAST-029: jsonb T_Null → INT
suite("repro_jt_cast_029") {
    def r = sql "SELECT CAST(CAST('null' AS JSONB) AS INT)"
    assertEquals(null, r[0][0], "JT-CAST-029: T_Null → SQL NULL; observed=${r}")
}
