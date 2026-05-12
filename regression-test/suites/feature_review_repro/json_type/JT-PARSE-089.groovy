// JT-PARSE-089: 嵌套 101 + default → default
suite("repro_jt_parse_089") {
    String s = "1"
    (1..101).each { s = "{\"a\":${s}}" }
    def r = sql "SELECT jsonb_parse_error_to_value('${s.replace("'","''")}', '[99]')"
    String v = r[0][0].toString()
    assertEquals("[99]", v, "JT-PARSE-089: deep input → default; observed=${r}")
}
