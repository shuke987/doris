suite("repro_ct_map_061") {
    def r = sql "SELECT element_at(map('a',1), 'A')"
    assertEquals(null, r[0][0], "CT-MAP-061: case sensitive -> NULL; observed=${r}")
}
