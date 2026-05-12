suite("repro_ct_array_165") {
    def r1 = sql "SELECT array_shuffle(array(1,2,3,4,5), 1)"
    def r2 = sql "SELECT array_shuffle(array(1,2,3,4,5), 1)"
    assertEquals(r1[0][0].toString(), r2[0][0].toString(), "CT-ARRAY-165: same seed reproducible; r1=${r1} r2=${r2}")
}
