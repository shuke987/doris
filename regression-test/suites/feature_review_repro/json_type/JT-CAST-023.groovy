// JT-CAST-023: jsonb T_String("42") → INT
suite("repro_jt_cast_023") {
    sql "SET enable_strict_cast=false"
    def r = sql "SELECT CAST(CAST('\"42\"' AS JSONB) AS INT)"
    sql "SET enable_strict_cast=default"
    String v = r[0][0]?.toString() ?: "null"
    // spec: non-strict: NULL OR 42 (deep cast TBD). lock observation.
    assertTrue(v == "42" || v == "null",
        "JT-CAST-023; observed=${r}")
}
