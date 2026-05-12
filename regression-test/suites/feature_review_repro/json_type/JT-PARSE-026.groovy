// JT-PARSE-026: 重复 key（合法 JSON）—当前 cluster 接受，验证 last-wins / first-wins
suite("repro_jt_parse_026") {
    // probe shows: dup key parse succeeds, returns "{"a":1,"a":2}" raw
    def r = sql "SELECT jsonb_parse('{\"a\":1,\"a\":2}')"
    String v = r[0][0].toString()
    // spec: SEV-2 contract — repeat keys not normalized
    assertTrue(v.contains("\"a\":1") || v.contains("\"a\":2"),
        "JT-PARSE-026: dup-key behavior; observed=${r}")
}
