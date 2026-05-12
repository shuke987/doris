// JT-COMPAT-017: json_quote/json_unquote (legacy)
suite("repro_jt_compat_017") {
    def r = sql """SELECT json_quote('hi')"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"hi"'), "JT-COMPAT-017; observed=${r}")
}
