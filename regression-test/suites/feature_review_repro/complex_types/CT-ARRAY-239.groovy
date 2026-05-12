suite("repro_ct_array_239") {
    def r = sql "SELECT element_at(array(1,2,3), CAST('170141183460469231731687303715884105727' AS LARGEINT))"
    assertEquals(null, r[0][0], "CT-ARRAY-239: LARGEINT extreme idx -> NULL (SEV-1 #2); observed=${r}")
}
