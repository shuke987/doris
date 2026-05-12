suite("repro_ct_map_072") {
    def r = sql "SELECT map_size(CAST(NULL AS MAP<STRING,INT>))"
    assertEquals(null, r[0][0], "CT-MAP-072: NULL map size=NULL; observed=${r}")
}
