suite("repro_ct_map_071") {
    def r = sql "SELECT map_size(map())"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-MAP-071: empty=0; observed=${r}")
}
