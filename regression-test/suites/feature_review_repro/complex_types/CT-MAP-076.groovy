suite("repro_ct_map_076") {
    def r = sql "SELECT map_keys(CAST(NULL AS MAP<STRING,INT>))"
    assertEquals(null, r[0][0], "CT-MAP-076: NULL map keys=NULL; observed=${r}")
}
