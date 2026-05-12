// JT-MODIFY-036: replace path 不存在
suite("repro_jt_modify_036") {
    def r = sql """SELECT json_replace(CAST('{"a":1}' AS JSONB), '\$.b', 2)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"a":1'), "JT-MODIFY-036; observed=${r}")
}
