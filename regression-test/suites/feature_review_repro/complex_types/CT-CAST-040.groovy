suite("repro_ct_cast_040") {
    def r = sql "SELECT map_size(CAST(map() AS MAP<STRING,BIGINT>))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-CAST-040: empty map cast; observed=${r}")
}
