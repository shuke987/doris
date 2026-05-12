suite("repro_ct_map_077") {
    def r = sql "SELECT array_size(map_keys(map('a',1,'a',2)))"
    long n = (r[0][0] as Number).longValue()
    // dedup keys after last-wins
    assertEquals(1L, n, "CT-MAP-077: dup key dedup keys size=1; observed=${r}")
}
