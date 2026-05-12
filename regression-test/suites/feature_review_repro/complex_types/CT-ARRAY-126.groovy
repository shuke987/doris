suite("repro_ct_array_126") {
    def r = sql "SELECT arrays_overlap(array(), array())"
    assertEquals(false, (r[0][0] as Boolean), "CT-ARRAY-126: empty overlap = false; observed=${r}")
}
