suite("repro_ct_array_082") {
    def r = sql "SELECT array(10,20,30)[1]"
    assertEquals(10, (r[0][0] as Number).intValue(), "CT-ARRAY-082: arr[1]; observed=${r}")
}
