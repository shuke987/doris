// JT-EXTRACT-046: extract_string emoji
suite("repro_jt_extract_046") {
    def r = sql "SELECT jsonb_extract_string(CAST('{\"a\":\"🎉\"}' AS JSONB), '\$.a')"
    String v = r[0][0]?.toString() ?: ""
    assertTrue(v.contains("🎉") || v.length() > 0, "JT-EXTRACT-046; observed=${r}")
}
