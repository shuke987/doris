// JT-MODIFY-019: set value 是 array
suite("repro_jt_modify_019") {
    def r = sql """SELECT json_set(CAST('{}' AS JSONB), '\$.a', CAST('[1,2]' AS JSONB))"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('[1,2]'), "observed=${r}")
}
