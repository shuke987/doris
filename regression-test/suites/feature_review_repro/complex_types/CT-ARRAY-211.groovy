suite("repro_ct_array_211") {
    def r = sql "SELECT array_avg(array(CAST(NULL AS INT), CAST(NULL AS INT)))"
    assertEquals(null, r[0][0], "CT-ARRAY-211: avg all NULL=NULL; observed=${r}")
}
