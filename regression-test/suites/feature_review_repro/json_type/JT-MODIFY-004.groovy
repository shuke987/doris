// JT-MODIFY-004: set 深嵌套 path 不存在（中间也不存在）
suite("repro_jt_modify_004") {
    def r = sql """SELECT json_set(CAST('{}' AS JSONB), '\$.a.b.c', 1)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('1'), "observed=${r}")
}
