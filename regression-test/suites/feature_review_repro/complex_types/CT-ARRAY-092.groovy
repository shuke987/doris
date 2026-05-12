suite("repro_ct_array_092") {
    def r = sql "SELECT element_at(array(1,2,3), CAST(2147483648 AS BIGINT))"
    assertEquals(null, r[0][0], "CT-ARRAY-092: BIGINT idx no truncation (SEV-1 #2); observed=${r}")
}
