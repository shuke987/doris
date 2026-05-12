// JT-EXTRACT-063: extract_double 对 decimal
suite("repro_jt_extract_063") {
    def r = sql "SELECT jsonb_extract_double(CAST('{\"a\":3.14}' AS JSONB), '\$.a')"
    String v = r[0][0]?.toString() ?: ""
    assertTrue(v.startsWith("3"), "JT-EXTRACT-063; observed=${r}")
}
