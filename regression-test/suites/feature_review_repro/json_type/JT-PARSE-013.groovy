// JT-PARSE-013: 嵌套 1000 层 应拒绝 + 不 crash — SEV-1 #2 守护
suite("repro_jt_parse_013") {
    String s = "1"
    (1..1000).each { s = "{\"a\":${s}}" }
    boolean threw = false
    try { sql "SELECT jsonb_parse('${s.replace("'","''")}')" }
    catch (Exception e) { threw = true }
    // BE must not crash; should reject
    assertTrue(threw,
        "JT-PARSE-013 (SEV-1 #2): 1000-level nested should be rejected (no crash); len=${s.length()}")
}
