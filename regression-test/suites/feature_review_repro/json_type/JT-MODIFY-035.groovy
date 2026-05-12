// JT-MODIFY-035: replace path 存在
suite("repro_jt_modify_035") {
    def r = sql """SELECT json_replace(CAST('{"a":1}' AS JSONB), '\$.a', 2)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"a":2'), "JT-MODIFY-035; observed=${r}")
}
