// JT-MODIFY-028: insert path 不存在
suite("repro_jt_modify_028") {
    def r = sql """SELECT json_insert(CAST('{"a":1}' AS JSONB), '\$.b', 2)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"b":2'), "JT-MODIFY-028; observed=${r}")
}
