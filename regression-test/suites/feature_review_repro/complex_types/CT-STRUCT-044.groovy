suite("repro_ct_struct_044") {
    def r = sql "SELECT named_struct('a',1,'b','x')"
    assertTrue(r[0][0] != null, "CT-STRUCT-044: named_struct; observed=${r}")
}
