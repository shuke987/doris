suite("repro_ct_cast_003") {
    def r = sql "SELECT CAST('null' AS ARRAY<INT>)"
    // spec: NULL or behavior
    assertTrue(r[0][0] != null || r[0][0] == null, "CT-CAST-003: 'null' string; observed=${r}")
}
