// JT-MODIFY-024: set path 大小写区分
suite("repro_jt_modify_024") {
    def r = sql """SELECT json_set(CAST('{"a":1}' AS JSONB), '\$.A', 2)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"A":2'), "JT-MODIFY-024; observed=${r}")
}
