// JT-EXTRACT-113: keys 中文/emoji
suite("repro_jt_extract_113") {
    def r = sql """SELECT json_keys(CAST('{"中文":1,"🎉":2}' AS JSONB))"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('中文'), "observed=${r}")
}
