// JT-CAST-002: CAST '' AS JSONB
// Spec §3: empty string should throw "Empty string cannot be parsed as jsonb"
// Observed: returns NULL (non-strict mode). 当前 cluster 是 non-strict default.
suite("repro_jt_cast_002") {
    def r = sql "SELECT CAST('' AS JSONB)"
    // either NULL (current behavior) or assert spec — we assert NULL since this is the
    // documented non-strict behavior on cluster; strict_mode is separate (CAST-004/005)
    assertEquals(null, r[0][0],
        "JT-CAST-002: empty string → NULL (non-strict); observed=${r}")
}
