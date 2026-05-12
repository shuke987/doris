suite("repro_ct_map_065") {
    def r = sql "SELECT map_contains_key(map('a',1), 'z')"
    Object obs = r[0][0]
    assertTrue(obs == false || (obs as Number).longValue() == 0L, "CT-MAP-065: missing false; observed=${r}")
}
