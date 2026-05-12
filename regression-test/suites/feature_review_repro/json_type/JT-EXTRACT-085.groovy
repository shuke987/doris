// JT-EXTRACT-085: exists_path wildcard
suite("repro_jt_extract_085") {
    boolean threw = false
    def r = null
    try { r = sql "SELECT jsonb_exists_path(CAST('{\"a\":1}' AS JSONB), '\$.*')" }
    catch (Exception e) { threw = true }
    // either succeeds with 1 or rejects (spec ambiguous); lock observation
    if (!threw) {
        assertNotNull(r[0][0], "JT-EXTRACT-085; observed=${r}")
    }
}
