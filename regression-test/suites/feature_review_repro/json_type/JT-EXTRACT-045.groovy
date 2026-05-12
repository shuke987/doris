// JT-EXTRACT-045: extract_string 中文值
suite("repro_jt_extract_045") {
    def r = sql "SELECT jsonb_extract_string(CAST('{\"a\":\"中文\"}' AS JSONB), '\$.a')"
    assertEquals("中文", r[0][0].toString(), "JT-EXTRACT-045; observed=${r}")
}
