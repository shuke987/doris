suite("repro_ct_cast_008") {
    def r = sql "SELECT CAST(NULL AS ARRAY<INT>)"
    assertEquals(null, r[0][0], "CT-CAST-008: NULL string; observed=${r}")
}
