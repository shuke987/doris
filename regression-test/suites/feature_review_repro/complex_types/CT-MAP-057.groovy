suite("repro_ct_map_057") {
    def r = sql "SELECT map('a',1,'b',2)['a']"
    assertEquals(1L, (r[0][0] as Number).longValue(), "CT-MAP-057: m['a']=1; observed=${r}")
}
