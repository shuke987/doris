// CT-ARRAY-057: array(NULL, NULL)
suite("repro_ct_array_057") {
    def r = sql "SELECT array_size(array(NULL, NULL))"
    assertEquals(2L, (r[0][0] as Number).longValue(), "CT-ARRAY-057: array(NULL, NULL); observed=${r}")
}
