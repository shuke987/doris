// JT-PARSE-072: parse_error_to_null 嵌套 101 → NULL
suite("repro_jt_parse_072") {
    String s = "1"
    (1..101).each { s = "{\"a\":${s}}" }
    def r = sql "SELECT jsonb_parse_error_to_null('${s.replace("'","''")}')"
    assertEquals(null, r[0][0],
        "JT-PARSE-072: 101-level → NULL (parse_error_to_null); observed=${r}")
}
