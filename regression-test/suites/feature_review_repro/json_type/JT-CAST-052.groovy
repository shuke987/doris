// JT-CAST-052: DOUBLE 3.14 → JSONB
suite("repro_jt_cast_052") {
    def r = sql "SELECT CAST(CAST(3.14 AS DOUBLE) AS JSONB)"
    String v = r[0][0].toString()
    assertTrue(v.startsWith("3.1"), "JT-CAST-052; observed=${r}")
}
