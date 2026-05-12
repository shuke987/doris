suite("repro_ct_map_068") {
    def r = sql "SELECT map_contains_value(map('a',1,'b',2), 1)"
    Object obs = r[0][0]
    assertTrue(obs == true || (obs as Number).longValue() == 1L, "CT-MAP-068: contains_value 1; observed=${r}")
}
