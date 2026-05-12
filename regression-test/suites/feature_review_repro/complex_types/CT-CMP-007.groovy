suite("repro_ct_cmp_007") {
    def r = sql "SELECT struct(1,'a') ORDER BY struct(1,'a')"
    assertTrue(r[0][0] != null, "CT-CMP-007: ORDER BY struct literal (SEV-1 #N1); observed=${r}")
}
