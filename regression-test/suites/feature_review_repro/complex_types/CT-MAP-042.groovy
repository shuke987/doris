suite("repro_ct_map_042") {
    def r = sql "SELECT map_size(map())"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-MAP-042: empty=0; observed=${r}")
}
