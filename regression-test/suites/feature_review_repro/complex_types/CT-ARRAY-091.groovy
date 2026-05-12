suite("repro_ct_array_091") {
    def r = sql "SELECT element_at(array(1,2,3), -2147483648)"
    assertEquals(null, r[0][0], "CT-ARRAY-091: element_at INT_MIN -> NULL no overflow (SEV-1 #2); observed=${r}")
}
