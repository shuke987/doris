suite("repro_ct_map_043") {
    def r = sql "SELECT map_size(map('a',1,'a',2))"
    long sz = (r[0][0] as Number).longValue()
    def r2 = sql "SELECT element_at(map('a',1,'a',2), 'a')"
    long v = (r2[0][0] as Number).longValue()
    // SEV-3 #N10 last-wins
    assertEquals(1L, sz, "CT-MAP-043: dup key dedup size=1 (SEV-3 #N10); observed sz=${r}")
    assertEquals(2L, v, "CT-MAP-043: last-wins v=2; observed=${r2}")
}
