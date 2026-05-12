// JT-EXTRACT-013: $[-1] 负索引 — spec SEV-2 #N7: 应拒绝非法 path
// 实际行为：cluster 接受并返回 last 元素 = SEV 复现
suite("repro_jt_extract_013") {
    def r = sql "SELECT jsonb_extract(CAST('[1,2,3]' AS JSONB), '\$[-1]')"
    // spec 期望：拒绝 (非法 path 语法 — 标准 jsonpath 不支持负索引)
    // 当前 cluster 返回 3 = SEV-2 #N7 reproduce
    assertEquals(null, r[0][0],
        "JT-EXTRACT-013 (SEV-2 #N7): negative index should be rejected as illegal path; observed=${r}")
}
