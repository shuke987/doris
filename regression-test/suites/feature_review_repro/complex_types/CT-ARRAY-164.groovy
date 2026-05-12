suite("repro_ct_array_164") {
    def r = sql "SELECT array_size(array_shuffle(array(1,2,3)))"
    assertEquals(3L, (r[0][0] as Number).longValue(), "CT-ARRAY-164: shuffle size preserved; observed=${r}")
}
