// JT-CAST-037: jsonb T_Array → ARRAY<INT>
suite("repro_jt_cast_037") {
    def r = sql "SELECT CAST(CAST('[1,2,3]' AS JSONB) AS ARRAY<INT>)"
    String v = r[0][0].toString()
    assertTrue(v.contains("1") && v.contains("2") && v.contains("3"),
        "JT-CAST-037; observed=${r}")
}
