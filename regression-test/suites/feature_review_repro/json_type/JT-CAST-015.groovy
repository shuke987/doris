// JT-CAST-015: T_String → string + jsonb_string_as_string=false
suite("repro_jt_cast_015") {
    def r = sql """SELECT CAST(CAST('"hi"' AS JSONB) AS STRING)"""
    assertNotNull(r[0][0], "JT-CAST-015; observed=${r}")
}
