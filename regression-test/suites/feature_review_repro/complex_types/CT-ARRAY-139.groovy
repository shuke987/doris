suite("repro_ct_array_139") {
    def r = sql "SELECT array_size(array_distinct(array()))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-139: distinct empty; observed=${r}")
}
