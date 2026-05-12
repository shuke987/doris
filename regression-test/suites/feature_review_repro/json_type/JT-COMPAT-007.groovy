// JT-COMPAT-007: JSON_KEYS 行为
suite("repro_jt_compat_007") {
    def r = sql """SELECT json_keys(CAST('{"a":1,"b":2}' AS JSONB))"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('a'), "observed=${r}")
}
