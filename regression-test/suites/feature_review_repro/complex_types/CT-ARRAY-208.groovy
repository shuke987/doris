suite("repro_ct_array_208") {
    def r = sql "SELECT array_sum(array(CAST('NaN' AS DOUBLE), 1.0))"
    // NaN result
    Object obs = r[0][0]
    assertTrue(obs != null, "CT-ARRAY-208: NaN sum no crash; observed=${r}")
}
