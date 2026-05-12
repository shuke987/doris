// JT-PARSE-037: key 1KB
suite("repro_jt_parse_037") {
    String k = 'a' * 1024
    boolean threw = false
    try { sql "SELECT jsonb_parse('{\"${k}\":1}')" } catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PARSE-037: 1KB key should reject")
}
