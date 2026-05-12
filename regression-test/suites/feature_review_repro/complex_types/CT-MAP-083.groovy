suite("repro_ct_map_083") {
    def r = sql "SELECT array_size(map_values(map()))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-MAP-083: empty map_values; observed=${r}")
}
