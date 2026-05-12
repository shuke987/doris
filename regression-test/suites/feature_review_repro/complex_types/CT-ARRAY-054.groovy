// CT-ARRAY-054: array() empty
suite("repro_ct_array_054") {
    def r = sql "SELECT array_size(array())"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-054: array() empty; observed=${r}")
}
