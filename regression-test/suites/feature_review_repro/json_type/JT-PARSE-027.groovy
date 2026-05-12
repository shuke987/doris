// JT-PARSE-027: UTF-8 中文 key
suite("repro_jt_parse_027") {
    def r = sql "SELECT jsonb_parse('{\"中文\":1}')"
    String val = r[0][0].toString()
    assertTrue(val.contains("中文") || val.contains("\\u"),
        "JT-PARSE-027: Chinese key should parse; observed=${r}")
}
