suite("repro_ct_cast_018") {
    def r = sql """SELECT map_size(CAST('{}' AS MAP<STRING,INT>))"""
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-CAST-018: empty MAP cast; observed=${r}")
}
