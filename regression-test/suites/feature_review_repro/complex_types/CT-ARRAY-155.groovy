suite("repro_ct_array_155") {
    def r = sql "SELECT array_size(array_compact(array()))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-155: compact empty; observed=${r}")
}
