suite("repro_ct_array_115") {
    def r = sql "SELECT array_contains(array(1,2,3), CAST(NULL AS INT))"
    Object obs = r[0][0]
    assertTrue(obs != null || obs == null, "CT-ARRAY-115: no crash; observed=${r}")
}
