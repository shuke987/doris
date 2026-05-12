suite("repro_ct_struct_060") {
    def r = sql "SELECT struct_element(named_struct('a',1,'b','x'), 'a')"
    assertEquals(1, (r[0][0] as Number).intValue(), "CT-STRUCT-060: by name 'a'; observed=${r}")
}
