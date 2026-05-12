suite("repro_ct_array_145") {
    def r = sql "SELECT array_size(array_intersect(array(1,2), array()))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-145: intersect with empty; observed=${r}")
}
