suite("repro_ct_array_120") {
    def r = sql "SELECT array_contains(array('Abc'), 'abc')"
    assertEquals(false, (r[0][0] as Boolean), "CT-ARRAY-120: case sensitive string; observed=${r}")
}
