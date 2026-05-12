suite("repro_ct_cast_015") {
    def r = sql "SELECT CAST('[\"中文\"]' AS ARRAY<STRING>)"
    assertTrue(r[0][0] != null, "CT-CAST-015: chinese cast; observed=${r}")
}
