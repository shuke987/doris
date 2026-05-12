suite("repro_ct_cast_002") {
    def r = sql "SELECT array_size(CAST('[]' AS ARRAY<INT>))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-CAST-002: empty cast; observed=${r}")
}
