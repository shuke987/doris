suite("repro_ct_cmp_006") {
    // SEV-1 #N1 MapLiteral.compareLiteral=0
    def r = sql "SELECT map('a',1) ORDER BY map('a',1)"
    assertTrue(r[0][0] != null, "CT-CMP-006: ORDER BY map literal (SEV-1 #N1 doc); observed=${r}")
}
