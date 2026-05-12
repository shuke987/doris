// JT-EXTRACT-024: path 引号包裹含空格 key
suite("repro_jt_extract_024") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a b\":1}' AS JSONB), '\$.\"a b\"')"
    assertEquals("1", r[0][0].toString(), "JT-EXTRACT-024; observed=${r}")
}
