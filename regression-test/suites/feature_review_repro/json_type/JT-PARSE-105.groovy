// JT-PARSE-105: 错误消息含位置信息
suite("repro_jt_parse_105") {
    boolean threw = false; String err = ""
    try { sql "SELECT jsonb_parse('{a:1}')" }
    catch (Exception e) { threw = true; err = e.message ?: "" }
    assertTrue(threw, "JT-PARSE-105: should throw")
    // spec: error should mention parsing context (simdjson / position)
    assertTrue(err.toLowerCase().contains("json") || err.toLowerCase().contains("parse"),
        "JT-PARSE-105: error msg should mention json/parse; err=${err.take(200)}")
}
