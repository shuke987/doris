suite("repro_ct_cast_001") {
    def r = sql "SELECT CAST('[1,2,3]' AS ARRAY<INT>)"
    String s = r[0][0].toString()
    assertTrue(s.contains("1") && s.contains("2") && s.contains("3"), "CT-CAST-001: string->ARRAY<INT>; observed=${r}")
}
