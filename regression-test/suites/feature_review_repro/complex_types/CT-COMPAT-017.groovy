suite("repro_ct_compat_017") {
    // doc check: Doris MAP last-wins consistency
    def r = sql "SELECT element_at(map('a',1,'a',2), 'a')"
    assertEquals(2L, (r[0][0] as Number).longValue(), "CT-COMPAT-017: MAP last-wins matches MySQL JSON (SEV-3 #N10); observed=${r}")
}
