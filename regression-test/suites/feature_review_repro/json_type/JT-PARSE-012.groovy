// JT-PARSE-012: 嵌套 101 层 应拒绝（writer reject）— SEV-1 #2 守护
suite("repro_jt_parse_012") {
    // build 101-level nested object
    String s = "1"
    (1..101).each { s = "{\"a\":${s}}" }
    boolean threw = false; String err = ""
    try { sql "SELECT jsonb_parse('${s.replace("'","''")}')" }
    catch (Exception e) { threw = true; err = e.message ?: "" }
    assertTrue(threw,
        "JT-PARSE-012 (SEV-1 #2 guard): 101-level nested should be rejected; len=${s.length()}")
}
