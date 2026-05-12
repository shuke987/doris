suite("repro_ct_cast_034") {
    def r = sql "SELECT CAST(array(1, CAST(NULL AS INT)) AS ARRAY<BIGINT>)"
    assertTrue(r[0][0] != null, "CT-CAST-034: NULL elem cast; observed=${r}")
}
