suite("repro_ct_array_206") {
    def r = sql "SELECT array_sum(array())"
    Object obs = r[0][0]
    // spec: 0 or NULL
    assertTrue(obs == null || (obs as Number).longValue() == 0L, "CT-ARRAY-206: empty sum spec 0/NULL; observed=${r}")
}
