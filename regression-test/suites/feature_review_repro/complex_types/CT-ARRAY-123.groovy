suite("repro_ct_array_123") {
    def r = sql "SELECT arrays_overlap(array(1,2,3), array(2,4))"
    assertEquals(true, (r[0][0] as Boolean), "CT-ARRAY-123: arrays_overlap true; observed=${r}")
}
