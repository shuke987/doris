suite("repro_ct_struct_025") {
    def r = sql "SELECT struct(1, 'a')"
    assertTrue(r[0][0] != null, "CT-STRUCT-025: struct literal; observed=${r}")
}
