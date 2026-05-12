suite("repro_ct_cast_039") {
    def r = sql "SELECT CAST(CAST(NULL AS MAP<STRING,INT>) AS MAP<STRING,BIGINT>)"
    assertEquals(null, r[0][0], "CT-CAST-039: NULL map cast; observed=${r}")
}
