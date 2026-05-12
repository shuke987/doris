// JT-CAST-050: BIGINT 2^62 → JSONB
suite("repro_jt_cast_050") {
    def r = sql "SELECT CAST(CAST(4611686018427387904 AS BIGINT) AS JSONB)"
    String v = r[0][0].toString()
    assertTrue(v.contains("4611686018427387904"), "JT-CAST-050; observed=${r}")
}
