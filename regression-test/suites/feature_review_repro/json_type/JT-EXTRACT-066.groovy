// JT-EXTRACT-066: extract_bool true
suite("repro_jt_extract_066") {
    def r = sql "SELECT jsonb_extract_bool(CAST('{\"a\":true}' AS JSONB), '\$.a')"
    String v = r[0][0].toString().toLowerCase()
    assertTrue(v == "true" || v == "1", "JT-EXTRACT-066; observed=${r}")
}
