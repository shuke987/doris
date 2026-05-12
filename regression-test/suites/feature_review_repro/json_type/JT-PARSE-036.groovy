// JT-PARSE-036: key 256 字节
suite("repro_jt_parse_036") {
    String k = 'a' * 256
    boolean threw = false
    try { sql "SELECT jsonb_parse('{\"${k}\":1}')" } catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PARSE-036: 256-byte key should reject")
}
