suite("repro_ct_map_101") {
    // basic map_contains_key without throw scenario
    def r = sql "SELECT map_contains_key(map('a',1), 'a')"
    Object obs = r[0][0]
    assertTrue(obs == true || (obs as Number).longValue() == 1L, "CT-MAP-101: basic contains; observed=${r}")
}
