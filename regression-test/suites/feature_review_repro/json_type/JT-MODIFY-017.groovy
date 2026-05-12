// JT-MODIFY-017: set 类型变化 int→string
suite("repro_jt_modify_017") {
    def r = sql """SELECT json_set(CAST('{"a":1}' AS JSONB), '\$.a', 'hi')"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"hi"'), "JT-MODIFY-017; observed=${r}")
}
