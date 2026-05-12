suite("repro_ct_array_178") {
    def r = sql "SELECT array_size(array_remove(array(1,2,3), 99))"
    assertEquals(3L, (r[0][0] as Number).longValue(), "CT-ARRAY-178: remove missing; observed=${r}")
}
