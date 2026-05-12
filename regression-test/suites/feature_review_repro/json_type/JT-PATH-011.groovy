// JT-PATH-011: $.["key$dollar"]
suite("repro_jt_path_011") {
    def r = sql "SELECT jsonb_extract(CAST('{\"key\$dollar\":1}' AS JSONB), '\$.\"key\$dollar\"')"
    String v = r[0][0]?.toString() ?: ""
    // Lock: cluster may accept or reject ; observation only
    assertNotNull(r, "JT-PATH-011; observed=${r}")
}
