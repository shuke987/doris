suite("repro_ct_map_058") {
    def r = sql "SELECT element_at(map('a',1), 'missing')"
    assertEquals(null, r[0][0], "CT-MAP-058: missing key -> NULL; observed=${r}")
}
