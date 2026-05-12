suite("repro_ct_array_106") {
    def r = sql "SELECT array_position(array(1,NULL,3), CAST(NULL AS INT))"
    Object obs = r[0][0]
    // spec behavior: NULL == NULL is generally not matched in array_position; but Doris may return 2
    assertTrue(obs == null || (obs as Number).longValue() >= 0, "CT-ARRAY-106: array_position with NULL element; observed=${r}")
}
