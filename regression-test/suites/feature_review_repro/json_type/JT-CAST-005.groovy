// JT-CAST-005: CAST 'invalid' AS JSONB + strict=false → NULL
suite("repro_jt_cast_005") {
    sql "SET enable_strict_cast=false"
    def r = sql "SELECT CAST('invalid' AS JSONB)"
    sql "SET enable_strict_cast=default"
    assertEquals(null, r[0][0],
        "JT-CAST-005: non-strict invalid → NULL; observed=${r}")
}
