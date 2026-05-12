suite("repro_ct_map_080") {
    def r = sql "SELECT array_size(map_keys(map('a',1,'b',2)))"
    assertEquals(2L, (r[0][0] as Number).longValue(), "CT-MAP-080: map_keys+array_size readonly XF; observed=${r}")
}
