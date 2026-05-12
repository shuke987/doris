suite("repro_ct_array_205") {
    def r = sql "SELECT array_sum(array(1,NULL,3))"
    Object obs = r[0][0]
    long n = (obs as Number).longValue()
    assertEquals(4L, n, "CT-ARRAY-205: sum NULL skipped =4; observed=${r}")
}
