suite("repro_ct_array_105") {
    def r = sql "SELECT array_position(array(1,2,3), CAST(NULL AS INT))"
    // spec: NULL -> 0 or NULL
    Object obs = r[0][0]
    assertTrue(obs == null || (obs as Number).longValue() == 0L, "CT-ARRAY-105: array_position(NULL) 0/NULL; observed=${r}")
}
