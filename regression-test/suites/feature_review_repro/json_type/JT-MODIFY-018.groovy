// JT-MODIFY-018: set value 是嵌套 jsonb
suite("repro_jt_modify_018") {
    def r = sql """SELECT json_set(CAST('{}' AS JSONB), '\$.a', CAST('{"x":1}' AS JSONB))"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"x":1'), "JT-MODIFY-018; observed=${r}")
}
