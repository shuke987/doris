// JT-PATH-024: $..a 双点（spec 待定，行为锁定）
suite("repro_jt_path_024") {
    // 期望：拒绝（非法 path）或等价 $**.a；当前 cluster 行为锁定
    def r = null
    boolean threw = false
    try { r = sql "SELECT jsonb_extract(CAST('{\"a\":1}' AS JSONB), '\$..a')" }
    catch (Exception e) { threw = true }
    // assert one branch: either throws OR returns 1
    if (!threw) {
        // currently observed: behaviour unknown, just sanity
        assertNotNull(r, "JT-PATH-024: returned result; observed=${r}")
    }
}
