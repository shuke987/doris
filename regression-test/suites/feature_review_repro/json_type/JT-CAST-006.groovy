// JT-CAST-006: CAST '   ' AS JSONB
suite("repro_jt_cast_006") {
    def r = sql """SELECT CAST('   ' AS JSONB)"""
    assertEquals(null, r[0][0], "JT-CAST-006: expect NULL; observed=${r}")
}
