suite("repro_ct_cast_017") {
    def r = sql """SELECT map_size(CAST('{"a":1,"b":2}' AS MAP<STRING,INT>))"""
    assertEquals(2L, (r[0][0] as Number).longValue(), "CT-CAST-017: string->MAP; observed=${r}")
}
