// JT-EXTRACT-110: jsonb_keys wildcard path 应拒绝
suite("repro_jt_extract_110") {
    boolean threw = false
    try { sql "SELECT jsonb_keys(CAST('{\"a\":1}' AS JSONB), '\$.*')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-EXTRACT-110: wildcard for keys should reject")
}
