suite("repro_ct_struct_056") {
    def r = sql "SELECT struct_element(struct(1, 'a'), 2)"
    assertEquals("a", r[0][0].toString(), "CT-STRUCT-056: element 2; observed=${r}")
}
