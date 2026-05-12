// JT-COMPAT-005: JSON_TYPE 返回值
suite("repro_jt_compat_005") {
    def r = sql """SELECT json_type(CAST('{}' AS JSONB), '\$')"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('object'), "observed=${r}")
}
