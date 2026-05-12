suite("repro_ct_cast_050") {
    def r = sql "SELECT CAST(CAST(NULL AS ARRAY<INT>) AS STRING)"
    assertEquals(null, r[0][0], "CT-CAST-050: NULL->STRING; observed=${r}")
}
