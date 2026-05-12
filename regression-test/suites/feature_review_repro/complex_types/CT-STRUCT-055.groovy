suite("repro_ct_struct_055") {
    def r = sql "SELECT struct_element(struct(1, 'a'), 1)"
    assertEquals(1, (r[0][0] as Number).intValue(), "CT-STRUCT-055: element 1; observed=${r}")
}
