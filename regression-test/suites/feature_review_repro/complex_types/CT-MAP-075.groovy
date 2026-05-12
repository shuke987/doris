suite("repro_ct_map_075") {
    def r = sql "SELECT array_size(map_keys(map()))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-MAP-075: empty map_keys=[]; observed=${r}")
}
