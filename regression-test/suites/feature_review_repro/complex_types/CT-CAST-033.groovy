suite("repro_ct_cast_033") {
    def r = sql "SELECT array_size(CAST(array() AS ARRAY<INT>))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-CAST-033: empty cast; observed=${r}")
}
