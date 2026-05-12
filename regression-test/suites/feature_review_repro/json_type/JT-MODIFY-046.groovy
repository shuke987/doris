// JT-MODIFY-046: remove 嵌套
suite("repro_jt_modify_046") {
    def r = sql """SELECT json_remove(CAST('{"a":{"b":1}}' AS JSONB), '\$.a.b')"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('{'), "observed=${r}")
}
