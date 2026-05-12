suite("repro_ct_cast_013") {
    def r = sql "SELECT CAST('[[1,2],[3,4]]' AS ARRAY<ARRAY<INT>>)"
    assertTrue(r[0][0] != null, "CT-CAST-013: nested cast; observed=${r}")
}
