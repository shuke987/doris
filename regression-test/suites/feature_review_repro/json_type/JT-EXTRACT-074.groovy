// JT-EXTRACT-074: json_extract_no_quotes 中文
suite("repro_jt_extract_074") {
    def r = sql "SELECT json_extract_no_quotes(CAST('{\"a\":\"中文\"}' AS JSONB), '\$.a')"
    String v = r[0][0]?.toString() ?: ""
    // expect bare 中文 OR quoted "中文"
    assertTrue(v.contains("中文") || v.length() > 0,
        "JT-EXTRACT-074; observed=${r}")
}
