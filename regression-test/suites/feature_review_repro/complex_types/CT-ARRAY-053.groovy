// CT-ARRAY-053: array(1,2,3)
suite("repro_ct_array_053") {
    def r = sql "SELECT array_size(array(1,2,3))"
    assertEquals(3L, (r[0][0] as Number).longValue(), "CT-ARRAY-053: array(1,2,3); observed=${r}")
}
