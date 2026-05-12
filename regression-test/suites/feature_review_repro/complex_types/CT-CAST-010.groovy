suite("repro_ct_cast_010") {
    def r = sql "SELECT CAST('[1,2,3]' AS ARRAY<BIGINT>)"
    String s = r[0][0].toString()
    assertTrue(s.contains("1") && s.contains("3"), "CT-CAST-010: BIGINT cast; observed=${r}")
}
