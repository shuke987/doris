// JT-EXTRACT-102: type corrupt — 无法直接复现，跳过实际 corrupt
suite("repro_jt_extract_102") {
    // type on valid jsonb works; corrupt construction not feasible in SQL
    def r = sql "SELECT jsonb_type(CAST('{\"a\":1}' AS JSONB), '\$.a')"
    assertEquals("int", r[0][0].toString(), "JT-EXTRACT-102 (lock); observed=${r}")
}
