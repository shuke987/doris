suite("repro_ct_map_081") {
    def r = sql "SELECT array_size(map_values(map('a',1,'b',2)))"
    assertEquals(2L, (r[0][0] as Number).longValue(), "CT-MAP-081: map_values size=2; observed=${r}")
}
