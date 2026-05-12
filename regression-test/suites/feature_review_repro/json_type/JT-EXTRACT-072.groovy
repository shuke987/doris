// JT-EXTRACT-072: json_extract_no_quotes int 值
suite("repro_jt_extract_072") {
    def r = sql "SELECT json_extract_no_quotes(CAST('{\"a\":1}' AS JSONB), '\$.a')"
    String v = r[0][0]?.toString() ?: ""
    // observed: returns "1" string of int
    assertTrue(v == "1" || v == "\"1\"",
        "JT-EXTRACT-072; observed=${r}")
}
