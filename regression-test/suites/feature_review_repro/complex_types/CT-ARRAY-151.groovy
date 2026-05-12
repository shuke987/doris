suite("repro_ct_array_151") {
    def r = sql "SELECT array_size(array_except(array(1,2,3), array(1,2,3)))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-151: except all=empty; observed=${r}")
}
