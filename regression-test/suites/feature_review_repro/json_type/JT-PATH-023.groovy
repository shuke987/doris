// JT-PATH-023: $a (no dot) 应拒绝
suite("repro_jt_path_023") {
    boolean threw = false
    try { sql "SELECT jsonb_extract(CAST('{\"a\":1}' AS JSONB), '\$a')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PATH-023: \$a without dot should reject")
}
