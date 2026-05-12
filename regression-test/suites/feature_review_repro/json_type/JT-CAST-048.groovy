// JT-CAST-048: 含 NULL ARRAY → JSONB
suite("repro_jt_cast_048") {
    def r = sql "SELECT CAST(ARRAY(1, NULL, 3) AS JSONB)"
    String v = r[0][0].toString()
    assertTrue(v.contains("null") || v.contains("NULL"),
        "JT-CAST-048: array NULL preserved as jsonb null; observed=${r}")
}
