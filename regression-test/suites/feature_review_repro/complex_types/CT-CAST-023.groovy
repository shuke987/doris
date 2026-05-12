suite("repro_ct_cast_023") {
    def r = sql "SELECT CAST(NULL AS MAP<STRING,INT>)"
    assertEquals(null, r[0][0], "CT-CAST-023: NULL string; observed=${r}")
}
