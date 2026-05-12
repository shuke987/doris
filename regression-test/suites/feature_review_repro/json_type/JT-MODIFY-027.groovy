// JT-MODIFY-027: insert path 存在
suite("repro_jt_modify_027") {
    def r = sql """SELECT json_insert(CAST('{"a":1}' AS JSONB), '\$.a', 2)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"a":1'), "JT-MODIFY-027; observed=${r}")
}
