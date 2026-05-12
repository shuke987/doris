suite("repro_ct_array_093") {
    def r = sql "SELECT element_at(array(1,2,3), CAST(-2147483649 AS BIGINT))"
    assertEquals(null, r[0][0], "CT-ARRAY-093: BIGINT min idx no overflow (SEV-1 #2); observed=${r}")
}
