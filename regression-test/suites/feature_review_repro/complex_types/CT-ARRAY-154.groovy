suite("repro_ct_array_154") {
    def r = sql "SELECT array_compact(array(NULL,NULL,1,NULL))"
    assertTrue(r[0][0] != null, "CT-ARRAY-154: compact NULL no crash; observed=${r}")
}
