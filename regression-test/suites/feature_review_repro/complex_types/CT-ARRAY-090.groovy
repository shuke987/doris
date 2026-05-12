suite("repro_ct_array_090") {
    def r = sql "SELECT element_at(array(1,2,3), 2147483647)"
    assertEquals(null, r[0][0], "CT-ARRAY-090: element_at INT_MAX -> NULL (SEV-1 #2); observed=${r}")
}
