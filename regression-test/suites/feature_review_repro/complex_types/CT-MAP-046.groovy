suite("repro_ct_map_046") {
    def r = sql "SELECT element_at(map('a', CAST(NULL AS INT)), 'a')"
    assertEquals(null, r[0][0], "CT-MAP-046: NULL value preserved; observed=${r}")
}
