// JT-EXTRACT-060: extract_largeint 对 int128 极值
suite("repro_jt_extract_060") {
    def r = sql "SELECT jsonb_extract_largeint(CAST('{\"a\":170141183460469231731687303715884105727}' AS JSONB), '\$.a')"
    String v = r[0][0]?.toString() ?: ""
    // 2^127-1 max int128
    assertTrue(v.contains("170141183460469231731687303715884105727"),
        "JT-EXTRACT-060; observed=${r}")
}
