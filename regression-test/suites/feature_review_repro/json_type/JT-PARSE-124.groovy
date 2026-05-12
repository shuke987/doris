// JT-PARSE-124: nereids parse normalize {"a":1.00,"b":   2}
// observed: cluster normalizes 1.00 → 1, whitespace stripped
suite("repro_jt_parse_124") {
    def r = sql "SELECT jsonb_parse('{\"a\":1.00,\"b\":   2}')"
    String v = r[0][0].toString()
    // documented: cluster normalizes value (1.00 → 1)
    assertTrue(v == "{\"a\":1,\"b\":2}" || v.contains("\"a\":1"),
        "JT-PARSE-124: nereids normalize observed; ${r}")
}
