suite("repro_ct_array_240") {
    def r = sql "SELECT element_at(array(1,2,3,4,5), CAST(-9223372036854775808 AS BIGINT))"
    assertEquals(null, r[0][0], "CT-ARRAY-240: INT64_MIN idx -> NULL (SEV-1 #2); observed=${r}")
}
