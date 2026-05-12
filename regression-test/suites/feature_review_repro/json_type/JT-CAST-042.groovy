// JT-CAST-042: ARRAY<INT> → JSONB
suite("repro_jt_cast_042") {
    def r = sql "SELECT CAST(ARRAY(1,2,3) AS JSONB)"
    String v = r[0][0].toString()
    assertTrue(v.startsWith("[") && v.contains("1") && v.contains("3"),
        "JT-CAST-042; observed=${r}")
}
