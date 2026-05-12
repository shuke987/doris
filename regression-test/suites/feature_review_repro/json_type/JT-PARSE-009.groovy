// JT-PARSE-009: empty object
suite("repro_jt_parse_009") {
    def r = sql "SELECT jsonb_parse('{}')"
    assertEquals("{}", r[0][0].toString(),
        "JT-PARSE-009: empty object; observed=${r}")
}
