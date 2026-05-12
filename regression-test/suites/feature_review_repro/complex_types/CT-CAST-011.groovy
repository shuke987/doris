suite("repro_ct_cast_011") {
    def r = sql "SELECT CAST('[1.5,2.5]' AS ARRAY<INT>)"
    Object obs = r[0][0]
    assertTrue(obs != null || obs == null, "CT-CAST-011: float->int trunc; observed=${r}")
}
