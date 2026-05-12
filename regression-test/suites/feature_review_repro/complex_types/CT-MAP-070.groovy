suite("repro_ct_map_070") {
    def r = sql "SELECT map_size(map('a',1,'b',2))"
    assertEquals(2L, (r[0][0] as Number).longValue(), "CT-MAP-070: map_size=2; observed=${r}")
}
