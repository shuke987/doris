// JT-EXTRACT-077: isnull 对非 null
suite("repro_jt_extract_077") {
    def r = sql "SELECT json_extract_isnull(CAST('{\"a\":1}' AS JSONB), '\$.a')"
    String v = r[0][0].toString().toLowerCase()
    assertTrue(v == "0" || v == "false", "JT-EXTRACT-077; observed=${r}")
}
