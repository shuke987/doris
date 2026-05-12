// JT-MODIFY-009: set 多 path
suite("repro_jt_modify_009") {
    def r = sql """SELECT json_set(CAST('{}' AS JSONB), '\$.a', 1, '\$.b', 2)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"b":2'), "JT-MODIFY-009; observed=${r}")
}
