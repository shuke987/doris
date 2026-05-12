suite("repro_ct_array_180") {
    def r = sql "SELECT array_size(array_concat(array(1,2), array()))"
    assertEquals(2L, (r[0][0] as Number).longValue(), "CT-ARRAY-180: concat with empty; observed=${r}")
}
