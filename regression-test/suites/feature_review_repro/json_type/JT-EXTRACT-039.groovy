// JT-EXTRACT-039: extract_string 对 int 值 — spec：类型不匹配应返 NULL（深 cast）
// 实际行为：当前 cluster 返回 "1" = SEV 复现（深 cast 应不强转）
suite("repro_jt_extract_039") {
    def r = sql "SELECT jsonb_extract_string(CAST('{\"a\":1}' AS JSONB), '\$.a')"
    // spec contract: deep cast does not coerce; should return NULL for non-string values
    assertEquals(null, r[0][0],
        "JT-EXTRACT-039 (SEV): extract_string on int should return NULL (no coercion); observed=${r}")
}
