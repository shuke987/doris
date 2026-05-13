// JT-EXTRACT-128 (HARD RULE): jsonb_exists_path with invalid path MUST reject clearly
suite("repro_jt_extract_128") {
    boolean threw = false; String err = ""
    try { sql "SELECT jsonb_exists_path(CAST('{\"a\":1}' AS JSONB), '\$.a', '!!!bad')" }
    catch (Exception e) { threw = true; err = e.message ?: "" }
    assertEquals(true, threw, "Invalid path syntax MUST be rejected; threw=${threw} err=${err}")
}
