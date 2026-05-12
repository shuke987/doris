// JT-PARSE-017: 空字符串报错
suite("repro_jt_parse_017") {
    boolean threw = false; String err = ""
    try { sql "SELECT jsonb_parse('')" }
    catch (Exception e) { threw = true; err = e.message ?: "" }
    assertTrue(threw, "JT-PARSE-017: empty string should throw; observed no error")
    assertTrue(err.toLowerCase().contains("empty") || err.toLowerCase().contains("json"),
        "JT-PARSE-017: error should mention empty/json; observed=${err.take(200)}")
}
