// JT-CONSTRUCT-056: jsonb_object key 256 字节错误消息
suite("repro_jt_construct_056") {
    String k = 'a' * 256
    boolean threw=false; String msg=''
    try { sql "SELECT json_object('${k}', 1)" } catch (Exception e) { threw=true; msg=e.message }
    assertTrue(threw, "JT-CONSTRUCT-056: should reject; msg=${msg}")
    // optional: msg should mention 256
}
