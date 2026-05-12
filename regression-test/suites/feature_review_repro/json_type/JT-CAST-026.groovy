// JT-CAST-026: jsonb T_Object → INT
suite("repro_jt_cast_026") {
    sql "SET enable_strict_cast=false"
    def r = sql "SELECT CAST(CAST('{\"a\":1}' AS JSONB) AS INT)"
    sql "SET enable_strict_cast=default"
    assertEquals(null, r[0][0],
        "JT-CAST-026: object → INT non-strict → NULL; observed=${r}")
}
