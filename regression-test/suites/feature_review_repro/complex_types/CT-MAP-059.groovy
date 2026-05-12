suite("repro_ct_map_059") {
    def r = sql "SELECT element_at(map('a',1), CAST(NULL AS STRING))"
    assertEquals(null, r[0][0], "CT-MAP-059: NULL key lookup -> NULL; observed=${r}")
}
