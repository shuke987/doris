// CT-ARRAY-056: array(1, NULL, 3)
suite("repro_ct_array_056") {
    def r = sql "SELECT array_size(array(1, NULL, 3))"
    assertEquals(3L, (r[0][0] as Number).longValue(), "CT-ARRAY-056: array(1, NULL, 3); observed=${r}")
}
