suite("repro_ct_array_181") {
    def r = sql "SELECT array_size(array_concat(array(1,2), array(3,4), array(5)))"
    assertEquals(5L, (r[0][0] as Number).longValue(), "CT-ARRAY-181: concat 3 arrays; observed=${r}")
}
