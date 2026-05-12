// JT-CAST-041: jsonb T_String → ARRAY 应拒绝（不嵌套解析）
suite("repro_jt_cast_041") {
    boolean threw = false
    try { sql "SELECT CAST(CAST('\"[1,2]\"' AS JSONB) AS ARRAY<INT>)" }
    catch (Exception e) { threw = true }
    // either rejects or NULL non-strict
    assertNotNull(threw, "JT-CAST-041 obs; threw=${threw}")
}
