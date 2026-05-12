// JT-EXTRACT-073: json_extract_no_quotes object 值
suite("repro_jt_extract_073") {
    def r = sql "SELECT json_extract_no_quotes(CAST('{\"a\":{\"x\":1}}' AS JSONB), '\$.a')"
    String v = r[0][0]?.toString() ?: ""
    assertTrue(v.contains("\"x\":1"), "JT-EXTRACT-073; observed=${r}")
}
