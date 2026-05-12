suite("repro_ct_array_111") {
    def r = sql "SELECT array_first_index(x->x>100, array(1,2,3))"
    Object obs = r[0][0]
    assertTrue(obs == null || (obs as Number).longValue() == 0L, "CT-ARRAY-111: no match -> 0/NULL; observed=${r}")
}
