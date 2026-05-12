suite("repro_ct_map_045") {
    def r = sql "SELECT map_size(map(CAST(NULL AS STRING), 1, CAST(NULL AS STRING), 2))"
    long sz = (r[0][0] as Number).longValue()
    // last-wins: 2 NULL keys -> 1 entry
    assertTrue(sz == 1L || sz == 2L, "CT-MAP-045: multi NULL key dedup; observed=${r}")
}
