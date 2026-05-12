suite("repro_ct_array_186") {
    def r = sql "SELECT array_size(array_pushback(array(1,2), CAST(NULL AS INT)))"
    assertEquals(3L, (r[0][0] as Number).longValue(), "CT-ARRAY-186: pushback NULL; observed=${r}")
}
