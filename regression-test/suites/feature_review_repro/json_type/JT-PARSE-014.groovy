// JT-PARSE-014: 嵌套 100 层 array
suite("repro_jt_parse_014") {
    String s = "[]"
    (1..50).each { s = "[${s}]" }
    def r = sql "SELECT jsonb_parse('${s}')"
    assertNotNull(r[0][0],
        "JT-PARSE-014: 50-level nested array; observed=${r}")
}
