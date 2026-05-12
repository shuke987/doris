// JT-EXTRACT-027: path 含 '.' key 用引号转义
suite("repro_jt_extract_027") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a.b\":1}' AS JSONB), '\$.\"a.b\"')"
    assertEquals("1", r[0][0].toString(), "JT-EXTRACT-027; observed=${r}")
}
