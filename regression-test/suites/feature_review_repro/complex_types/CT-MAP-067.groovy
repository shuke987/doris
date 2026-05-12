suite("repro_ct_map_067") {
    def r = sql "SELECT map_contains_key(map('中文',1), '中文')"
    Object obs = r[0][0]
    assertTrue(obs == true || (obs as Number).longValue() == 1L, "CT-MAP-067: chinese key match; observed=${r}")
}
