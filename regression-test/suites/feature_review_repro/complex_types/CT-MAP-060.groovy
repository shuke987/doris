suite("repro_ct_map_060") {
    def r = sql "SELECT element_at(CAST(NULL AS MAP<STRING,INT>), 'a')"
    assertEquals(null, r[0][0], "CT-MAP-060: NULL map lookup -> NULL; observed=${r}")
}
