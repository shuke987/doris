// JT-MODIFY-048: remove 多 path 全存在
suite("repro_jt_modify_048") {
    def r = sql """SELECT json_remove(CAST('{"a":1,"b":2,"c":3}' AS JSONB), '\$.a', '\$.b')"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"c":3'), "JT-MODIFY-048; observed=${r}")
}
