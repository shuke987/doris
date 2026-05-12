suite("repro_ct_array_124") {
    def r = sql "SELECT arrays_overlap(array(1,2,3), array(4,5))"
    assertEquals(false, (r[0][0] as Boolean), "CT-ARRAY-124: no overlap = false; observed=${r}")
}
