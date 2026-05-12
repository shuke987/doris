suite("repro_ct_map_066") {
    def r = sql "SELECT map_contains_key(map('a',1), CAST(NULL AS STRING))"
    Object obs = r[0][0]
    assertTrue(obs == null || obs == false || (obs as Number).longValue() == 0L, "CT-MAP-066: NULL lookup; observed=${r}")
}
