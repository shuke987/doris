suite("repro_ct_array_153") {
    def r = sql "SELECT array_size(array_compact(array(1,1,2,2,3,1)))"
    assertEquals(4L, (r[0][0] as Number).longValue(), "CT-ARRAY-153: compact consecutive; observed=${r}")
}
