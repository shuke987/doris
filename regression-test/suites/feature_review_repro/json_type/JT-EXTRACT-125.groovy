// JT-EXTRACT-125: json_extract 第 1 参非 JSONB → 自动 cast 或报错
suite("repro_jt_extract_125") {
    def r = null; boolean threw = false
    try { r = sql "SELECT json_extract(CAST('{\"a\":1}' AS STRING), '\$.a')" }
    catch (Exception e) { threw = true }
    if (!threw) {
        assertEquals("1", r[0][0].toString(),
            "JT-EXTRACT-125: implicit string→jsonb cast; observed=${r}")
    }
}
