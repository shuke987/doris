suite("repro_ct_array_114") {
    def r = sql "SELECT array_contains(array(1,NULL,3), CAST(NULL AS INT))"
    // spec: NULL match behavior
    Object obs = r[0][0]
    assertTrue(obs != null, "CT-ARRAY-114: NULL match returns boolean (not crash); observed=${r}")
}
