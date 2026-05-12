// JT-PATH-008: $.arr[0].key 混合
suite("repro_jt_path_008") {
    def r = sql "SELECT jsonb_extract(CAST('{\"arr\":[{\"k\":1},{\"k\":2}]}' AS JSONB), '\$.arr[0].k')"
    assertEquals("1", r[0][0].toString(), "JT-PATH-008; observed=${r}")
}
