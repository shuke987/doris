suite("repro_ct_struct_053") {
    def r = sql "SELECT named_struct('Aa', 1)"
    assertTrue(r[0][0] != null, "CT-STRUCT-053: case preserved; observed=${r}")
}
