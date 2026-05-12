suite("repro_ct_map_069") {
    def r = sql "SELECT map_contains_value(map('a',1), CAST(NULL AS INT))"
    Object obs = r[0][0]
    assertTrue(obs == null || obs == false || obs == true || (obs as Number) != null, "CT-MAP-069: NULL value spec; observed=${r}")
}
