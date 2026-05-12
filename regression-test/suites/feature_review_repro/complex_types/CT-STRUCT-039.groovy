suite("repro_ct_struct_039") {
    def r = sql "SELECT struct(1, 'a', 3.14)"
    assertTrue(r[0][0] != null, "CT-STRUCT-039: struct 3 args; observed=${r}")
}
