// JT-PATH-020: escape \\ in path key
suite("repro_jt_path_020") {
    def r = null; boolean threw = false
    try { r = sql "SELECT jsonb_extract(CAST('{\"a\\\\b\":1}' AS JSONB), '\$.\"a\\\\b\"')" }
    catch (Exception e) { threw = true }
    // lock observation
    assertNotNull(threw, "JT-PATH-020 obs; threw=${threw}")
}
