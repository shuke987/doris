// JT-EXTRACT-025: path 空格 key 无引号
suite("repro_jt_extract_025") {
    boolean threw = false
    try { sql "SELECT jsonb_extract(CAST('{\"a b\":1}' AS JSONB), '\$.a b')" }
    catch (Exception e) { threw = true }
    // path with space in unquoted key should reject
    assertTrue(threw, "JT-EXTRACT-025: unquoted space in key should reject")
}
