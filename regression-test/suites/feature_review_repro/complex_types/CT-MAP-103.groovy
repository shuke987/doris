suite("repro_ct_map_103") {
    def r = sql "SELECT map_size(str_to_map('a:1,b:2', ',', ':'))"
    assertEquals(2L, (r[0][0] as Number).longValue(), "CT-MAP-103: str_to_map size=2; observed=${r}")
}
