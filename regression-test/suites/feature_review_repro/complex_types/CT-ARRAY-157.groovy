suite("repro_ct_array_157") {
    def r = sql "SELECT array_sort(array(3,NULL,1))"
    assertTrue(r[0][0] != null, "CT-ARRAY-157: sort with NULL no crash; observed=${r}")
}
