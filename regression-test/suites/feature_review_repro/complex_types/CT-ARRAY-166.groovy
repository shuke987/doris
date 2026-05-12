suite("repro_ct_array_166") {
    def r = sql "SELECT array_size(array_shuffle(array()))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-166: shuffle empty; observed=${r}")
}
