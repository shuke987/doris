// JT-EXTRACT-127: jsonb_keys super_wildcard path 应拒绝 (SEV-2 #N7)
suite("repro_jt_extract_127") {
    boolean threw = false
    try { sql "SELECT jsonb_keys(CAST('{\"a\":1}' AS JSONB), '\$**.a')" }
    catch (Exception e) { threw = true }
    assertTrue(threw,
        "JT-EXTRACT-127 (SEV-2 #N7 class): super_wildcard path for keys should reject")
}
