suite("repro_ct_cross_083") {
    def r = sql "SELECT COALESCE(CAST(NULL AS ARRAY<INT>), array(99))"
    String s = r[0][0].toString()
    assertTrue(s.contains("99"), "CT-CROSS-083: COALESCE NULL array; observed=${r}")
}
