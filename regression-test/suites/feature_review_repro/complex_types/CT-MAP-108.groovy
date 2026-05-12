suite("repro_ct_map_108") {
    def r = sql "SELECT str_to_map(CAST(NULL AS STRING), ',', ':')"
    assertEquals(null, r[0][0], "CT-MAP-108: NULL str -> NULL; observed=${r}")
}
