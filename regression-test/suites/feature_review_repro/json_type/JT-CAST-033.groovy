// JT-CAST-033: jsonb → DOUBLE
suite("repro_jt_cast_033") {
    def r = sql "SELECT CAST(CAST('3.14' AS JSONB) AS DOUBLE)"
    String v = r[0][0].toString()
    assertTrue(v.startsWith("3.14"), "JT-CAST-033; observed=${r}")
}
