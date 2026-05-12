// JT-PATH-025: $.a[非法字符]
suite("repro_jt_path_025") {
    boolean threw = false
    try { sql "SELECT jsonb_extract(CAST('{\"a\":1}' AS JSONB), '\$.a[abc]')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PATH-025: illegal array index syntax should reject")
}
