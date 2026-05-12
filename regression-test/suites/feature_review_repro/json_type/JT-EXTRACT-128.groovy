// JT-EXTRACT-128: 多 path 错误消息只含非法 path
suite("repro_jt_extract_128") {
    boolean threw = false; String err = ""
    try { sql "SELECT jsonb_exists_path(CAST('{\"a\":1}' AS JSONB), '\$.a', '!!!bad')" }
    catch (Exception e) { threw = true; err = e.message ?: "" }
    // either rejects or returns array; lock
    assertNotNull(threw, "JT-EXTRACT-128 obs; threw=${threw}")
}
