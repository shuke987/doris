suite("repro_ct_array_152") {
    def r = sql "SELECT array_except(array(1,NULL), array(1))"
    assertTrue(r[0][0] != null, "CT-ARRAY-152: except preserve NULL no crash; observed=${r}")
}
