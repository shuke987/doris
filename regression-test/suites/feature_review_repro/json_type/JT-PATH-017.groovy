// JT-PATH-017: $** super_wildcard bare — 当前 cluster 拒绝 (must follow with leg)
// spec 描述 $** 为 super_wildcard，但 bare $** 实际行为是拒绝
suite("repro_jt_path_017") {
    boolean threw = false; String err = ""
    try { sql "SELECT jsonb_extract(CAST('{\"a\":{\"b\":1}}' AS JSONB), '\$**')" }
    catch (Exception e) { threw = true; err = e.message ?: "" }
    // observed: cluster rejects bare $** as "Invalid Json Path"
    assertTrue(threw, "JT-PATH-017: bare \$** rejected; err=${err.take(120)}")
}
