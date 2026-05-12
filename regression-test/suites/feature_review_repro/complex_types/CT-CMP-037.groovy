suite("repro_ct_cmp_037") {
    def r = sql "SELECT array(1,2) UNION ALL SELECT array(3,4)"
    assertEquals(2, r.size(), "CT-CMP-037: UNION ALL array; observed=${r}")
}
