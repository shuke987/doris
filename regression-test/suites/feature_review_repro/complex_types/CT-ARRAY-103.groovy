suite("repro_ct_array_103") {
    def r = sql "SELECT array_position(array(1,2,3), 2)"
    assertEquals(2L, (r[0][0] as Number).longValue(), "CT-ARRAY-103: array_position=2; observed=${r}")
}
