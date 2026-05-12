suite("repro_ct_map_064") {
    def r = sql "SELECT map_contains_key(map('a',1,'b',2), 'a')"
    Object obs = r[0][0]
    assertTrue(obs == true || (obs as Number).longValue() == 1L, "CT-MAP-064: contains_key true; observed=${r}")
}
