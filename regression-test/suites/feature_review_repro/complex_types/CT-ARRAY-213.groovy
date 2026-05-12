suite("repro_ct_array_213") {
    def r = sql "SELECT array_min(array(1,NULL,2))"
    assertEquals(1, (r[0][0] as Number).intValue(), "CT-ARRAY-213: min skip NULL=1; observed=${r}")
}
