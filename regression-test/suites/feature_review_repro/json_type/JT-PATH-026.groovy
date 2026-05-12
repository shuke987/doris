// JT-PATH-026: $.a[] missing index
suite("repro_jt_path_026") {
    boolean threw = false
    try { sql "SELECT jsonb_extract(CAST('{\"a\":[1]}' AS JSONB), '\$.a[]')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PATH-026: empty brackets should reject")
}
