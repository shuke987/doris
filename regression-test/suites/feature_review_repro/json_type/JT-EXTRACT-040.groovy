// JT-EXTRACT-040: extract_string 对 bool
suite("repro_jt_extract_040") {
    def r = sql "SELECT jsonb_extract_string(CAST('{\"a\":true}' AS JSONB), '\$.a')"
    assertEquals(null, r[0][0],
        "JT-EXTRACT-040 (SEV): extract_string on bool should return NULL; observed=${r}")
}
