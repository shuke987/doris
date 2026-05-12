// CT-ARRAY-074: array_range(NULL)
suite("repro_ct_array_074") {
    def r = sql "SELECT array_range(NULL)"
    assertEquals(null, r[0][0], "CT-ARRAY-074: array_range(NULL); observed=${r}")
}
