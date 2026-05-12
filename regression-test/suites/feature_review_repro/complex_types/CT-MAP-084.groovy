suite("repro_ct_map_084") {
    def r = sql "SELECT map_values(CAST(NULL AS MAP<STRING,INT>))"
    assertEquals(null, r[0][0], "CT-MAP-084: NULL map values=NULL; observed=${r}")
}
