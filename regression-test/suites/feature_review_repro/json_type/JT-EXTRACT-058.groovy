// JT-EXTRACT-058: extract_bigint 对 int128 over BIGINT 范围 → NULL
suite("repro_jt_extract_058") {
    def r = sql "SELECT jsonb_extract_bigint(CAST('{\"a\":36893488147419103232}' AS JSONB), '\$.a')"
    // 2^65 over bigint range; expect NULL
    assertEquals(null, r[0][0],
        "JT-EXTRACT-058: bigint overflow → NULL; observed=${r}")
}
