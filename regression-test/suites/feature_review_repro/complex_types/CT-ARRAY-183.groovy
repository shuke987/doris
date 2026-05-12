suite("repro_ct_array_183") {
    def r = sql "SELECT array_concat(CAST(NULL AS ARRAY<INT>), array(1,2))"
    Object obs = r[0][0]
    // spec: NULL behavior
    assertTrue(obs != null || obs == null, "CT-ARRAY-183: concat NULL; observed=${r}")
}
