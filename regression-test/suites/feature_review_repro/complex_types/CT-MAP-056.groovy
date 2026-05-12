suite("repro_ct_map_056") {
    def r = sql "SELECT element_at(map('a',1,'b',2), 'a')"
    assertEquals(1L, (r[0][0] as Number).longValue(), "CT-MAP-056: element_at a=1; observed=${r}")
}
