// JT-CAST-053: DECIMAL → JSONB
suite("repro_jt_cast_053") {
    def r = sql "SELECT CAST(CAST(3.14 AS DECIMAL(10,2)) AS JSONB)"
    String v = r[0][0].toString()
    assertTrue(v.contains("3.14") || v.startsWith("3.1"),
        "JT-CAST-053; observed=${r}")
}
