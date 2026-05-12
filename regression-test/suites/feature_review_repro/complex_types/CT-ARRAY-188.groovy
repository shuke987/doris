suite("repro_ct_array_188") {
    def r = sql "SELECT array_size(array_pushfront(array(1.0, 2.0), 0.5))"
    assertEquals(3L, (r[0][0] as Number).longValue(), "CT-ARRAY-188: pushfront promote; observed=${r}")
}
