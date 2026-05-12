// JT-MODIFY-005: set 数组元素存在
suite("repro_jt_modify_005") {
    def r = sql """SELECT json_set(CAST('[1,2,3]' AS JSONB), '\$[1]', 99)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('99'), "observed=${r}")
}
