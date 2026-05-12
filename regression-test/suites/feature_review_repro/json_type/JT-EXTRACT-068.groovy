// JT-EXTRACT-068: extract_bool 对 int 0/1 — spec: NULL (深 cast 不强转)
suite("repro_jt_extract_068") {
    def r = sql "SELECT jsonb_extract_bool(CAST('{\"a\":1}' AS JSONB), '\$.a')"
    assertEquals(null, r[0][0],
        "JT-EXTRACT-068 (SEV): extract_bool on int should NULL; observed=${r}")
}
