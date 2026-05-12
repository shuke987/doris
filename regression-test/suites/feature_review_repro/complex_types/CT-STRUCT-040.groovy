suite("repro_ct_struct_040") {
    def r = sql "SELECT struct(1)"
    assertTrue(r[0][0] != null, "CT-STRUCT-040: 1-arg struct; observed=${r}")
}
