suite("repro_ct_array_177") {
    def r = sql "SELECT array_remove(array(NULL,1,NULL), CAST(NULL AS INT))"
    assertTrue(r[0][0] != null, "CT-ARRAY-177: remove NULL no crash; observed=${r}")
}
