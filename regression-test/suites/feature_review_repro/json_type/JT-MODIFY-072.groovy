// JT-MODIFY-072: `json_set(j, '$**.a', val)` super_wildcard
suite("repro_jt_modify_072") {
    boolean threw = false
    try { sql """SELECT json_set(CAST('{\"a\":1}' AS JSONB), '\$**.a', 9)""" } catch (Exception e) { threw = true }
    assertTrue(threw, "JT-MODIFY-072: should reject; observed=no exception")
}
