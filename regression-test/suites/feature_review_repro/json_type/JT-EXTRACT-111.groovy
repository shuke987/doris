// JT-EXTRACT-111: keys super_wildcard path
suite("repro_jt_extract_111") {
    boolean threw = false
    try { sql """SELECT json_keys(CAST('{\"a\":1}' AS JSONB), '\$**')""" } catch (Exception e) { threw = true }
    assertTrue(threw, "JT-EXTRACT-111: should reject; observed=no exception")
}
