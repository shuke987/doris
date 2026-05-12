// JT-EXTRACT-069: extract_bool 对 string "true"
suite("repro_jt_extract_069") {
    def r = sql "SELECT jsonb_extract_bool(CAST('{\"a\":\"true\"}' AS JSONB), '\$.a')"
    assertEquals(null, r[0][0],
        "JT-EXTRACT-069 (SEV): extract_bool on string should NULL; observed=${r}")
}
