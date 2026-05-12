// JT-MODIFY-037: replace 数组元素
suite("repro_jt_modify_037") {
    def r = sql """SELECT json_replace(CAST('[1,2,3]' AS JSONB), '\$[0]', 9)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('9'), "observed=${r}")
}
