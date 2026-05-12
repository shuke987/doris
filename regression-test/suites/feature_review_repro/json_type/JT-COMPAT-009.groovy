// JT-COMPAT-009: JSON_SET / INSERT / REPLACE 多 path 顺序
suite("repro_jt_compat_009") {
    def r = sql """SELECT json_set(CAST('{}' AS JSONB), '\$.a', 1, '\$.b', 2)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"a":1'), "JT-COMPAT-009; observed=${r}")
}
