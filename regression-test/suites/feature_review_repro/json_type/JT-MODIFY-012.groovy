// JT-MODIFY-012: set NULL jsonb
suite("repro_jt_modify_012") {
    def r = sql """SELECT json_set(CAST(NULL AS JSONB), '\$.a', 1)"""
    assertEquals(null, r[0][0], "JT-MODIFY-012: expect NULL; observed=${r}")
}
