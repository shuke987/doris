suite("repro_ct_array_149") {
    def r = sql "SELECT array_union(array(NULL,1), array(1,NULL))"
    assertTrue(r[0][0] != null, "CT-ARRAY-149: union with NULL no crash; observed=${r}")
}
