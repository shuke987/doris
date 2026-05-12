// JT-EXTRACT-053: extract_int 对 double 3.7 — observation
suite("repro_jt_extract_053") {
    def r = sql "SELECT jsonb_extract_int(CAST('{\"a\":3.7}' AS JSONB), '\$.a')"
    String v = r[0][0]?.toString() ?: "null"
    // observed: cluster truncates 3.7 → 3
    assertTrue(v == "3" || v == "4" || v == "null",
        "JT-EXTRACT-053: extract_int on 3.7; observed=${r}")
}
