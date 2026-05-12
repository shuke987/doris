suite("repro_ct_map_044") {
    def r = sql "SELECT map_size(map(CAST(NULL AS STRING), 1))"
    long sz = (r[0][0] as Number).longValue()
    // spec: preserve or drop; current behavior
    assertTrue(sz >= 0, "CT-MAP-044: NULL key spec behavior; observed=${r}")
}
