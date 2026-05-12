// JT-MODIFY-025: set 同 path 在多 path 模式下出现两次
suite("repro_jt_modify_025") {
    def r = sql """SELECT json_set(CAST('{}' AS JSONB), '\$.a', 1, '\$.a', 2)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"a":2'), "JT-MODIFY-025; observed=${r}")
}
