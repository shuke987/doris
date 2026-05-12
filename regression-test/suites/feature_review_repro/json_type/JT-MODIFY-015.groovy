// JT-MODIFY-015: set value 是 jsonb null
suite("repro_jt_modify_015") {
    def r = sql """SELECT json_set(CAST('{}' AS JSONB), '\$.a', CAST('null' AS JSONB))"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('null'), "observed=${r}")
}
